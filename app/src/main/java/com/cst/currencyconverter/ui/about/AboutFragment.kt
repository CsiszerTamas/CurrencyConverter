package com.cst.currencyconverter.ui.about

import android.os.Bundle
import android.view.View
import com.cst.currencyconverter.R
import com.cst.currencyconverter.databinding.FragmentAboutBinding
import com.cst.currencyconverter.ui.base.BaseFragment

class AboutFragment : BaseFragment<FragmentAboutBinding, AboutViewModel>(
    R.layout.fragment_about,
    AboutViewModel::class
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentAboutToolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}
