package com.cst.currencyconverter.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cst.currencyconverter.R
import com.cst.currencyconverter.di.util.viewModel
import org.koin.core.qualifier.TypeQualifier
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseFragment<B : ViewDataBinding, out VM : ViewModel>(
    private var layoutResourceId: Int,
    vmClass: KClass<VM>
) :
    Fragment() {

    protected lateinit var binding: B
    protected open val viewModel: VM by viewModel(vmClass, qualifier = TypeQualifier(vmClass))
    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supportFragmentManager = requireActivity().supportFragmentManager

        try {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            navController = navHostFragment.navController
        } catch (ex: IllegalStateException) {
            Timber.e(ex)
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::navController.isInitialized) {
            try {
                val supportFragmentManager = requireActivity().supportFragmentManager
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
                navController = navHostFragment.navController
            } catch (ex: IllegalStateException) {
                Timber.e(ex)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)

        return binding.root
    }

    protected open fun onBackPressed() {
        navController.popBackStack()
    }
}
