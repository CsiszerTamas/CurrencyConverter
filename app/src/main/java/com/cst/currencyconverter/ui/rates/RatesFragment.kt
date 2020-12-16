package com.cst.currencyconverter.ui.rates

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cst.currencyconverter.MainActivity
import com.cst.currencyconverter.R
import com.cst.currencyconverter.constants.Constants
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.databinding.FragmentRatesBinding
import com.cst.currencyconverter.ui.base.BaseFragment
import com.cst.currencyconverter.ui.rates.adapter.RateAdapterContract
import com.cst.currencyconverter.ui.rates.adapter.RatesAdapter
import com.google.android.material.snackbar.Snackbar

class RatesFragment : BaseFragment<FragmentRatesBinding, RatesViewModel>(
    R.layout.fragment_rates,
    RatesViewModel::class
), RateAdapterContract {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snackBar = setupNoNetworkErrorMessage()
        setupRecyclerView(viewModel)

        setupObservers(snackBar)

        setupClickListeners()
    }

    private fun setupObservers(snackBar: Snackbar) {
        viewModel.getRates().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.ratesRecyclerView.visibility = View.VISIBLE

                    snackBar.dismiss()

                    (binding.ratesRecyclerView.adapter as RatesAdapter).rates =
                        resource.data?.rates?.toMutableList()!!
                }
                Resource.Status.LOADING -> {
                    binding.ratesRecyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    if (viewModel.sharedPreferenceManager.ratesCachedLocally) {
                        // We already have cached rates
                        if (!viewModel.errorMessageShown) {
                            Snackbar.make(
                                binding.ratesText,
                                getString(R.string.viewing_cached_data),
                                Snackbar.LENGTH_LONG
                            ).show()
                            viewModel.errorMessageShown = true
                        }
                    } else {
                        // We don't have cached rates yet so we show error Snackbar
                        if (!viewModel.errorMessageShown) {
                            binding.progressBar.visibility = View.GONE

                            snackBar.show()
                            viewModel.errorMessageShown = true

                            snackBar.setAction(getString(R.string.retry)) {
                                snackBar.dismiss()
                                binding.progressBar.visibility = View.VISIBLE
                                viewModel.errorMessageShown = false
                            }
                            snackBar.setActionTextColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.white,
                                    null
                                )
                            )
                        }
                    }
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.ratesText.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.light -> {
                    setLightMode()
                    true
                }
                R.id.dark -> {
                    setDarkMode()
                    true
                }
                R.id.about -> {
                    navigateToAboutFragment()
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.ratesText.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    private fun navigateToAboutFragment() {
        val direction = RatesFragmentDirections.actionRatesFragmentToAboutFragment()
        navController.navigate(direction)
    }

    private fun setDarkMode() {
        viewModel.sharedPreferenceManager.nightModeCode = Constants.DARK_MODE
        (requireActivity() as MainActivity).configureAppTheme()
    }

    private fun setLightMode() {
        viewModel.sharedPreferenceManager.nightModeCode = Constants.LIGHT_MODE
        (requireActivity() as MainActivity).configureAppTheme()
    }

    private fun setupNoNetworkErrorMessage(): Snackbar {
        return Snackbar.make(
            binding.ratesText,
            getString(R.string.no_internet_error_message),
            Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun setupRecyclerView(viewModel: RatesViewModel) {
        binding.ratesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RatesAdapter(viewModel::enterCurrencyValue, this)
        adapter.setHasStableIds(true)
        binding.ratesRecyclerView.adapter = adapter
    }

    override fun onBaseRateChanged() {
        binding.ratesRecyclerView.layoutManager?.scrollToPosition(0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        requireActivity().finish()
    }
}
