package com.cst.currencyconverter.ui.rates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.data.local.CurrencyDao
import com.cst.currencyconverter.data.local.Rate
import com.cst.currencyconverter.data.model.BasicCurrencyRate
import com.cst.currencyconverter.data.model.RatesData
import com.cst.currencyconverter.data.repositories.currency.CurrencyRateRepository
import com.cst.currencyconverter.ui.base.BaseViewModel
import com.cst.currencyconverter.utils.CurrencyItemDecoratorService
import com.cst.currencyconverter.utils.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import java.math.BigDecimal
import java.math.RoundingMode

class RatesViewModel(
    private val database: CurrencyDao,
    val sharedPreferenceManager: SharedPreferenceManager,
    private val currencyRateRepository: CurrencyRateRepository,
    val currencyDecoratorService: CurrencyItemDecoratorService
) : BaseViewModel(), KoinComponent {

    companion object {
        private const val SCALE = 3
    }

    private val manuallyEnteredValues = MutableLiveData<Resource<RatesData>>()
    private var latestNetworkState: RatesData? = null
    private var selectedCurrency: String? = null
    private var selectedAmount: BigDecimal? = null

    var errorMessageShown = false

    fun getRates(): LiveData<Resource<RatesData>> {

        val updatedRates = MediatorLiveData<Resource<RatesData>>()

        // Network updates
        updatedRates.addSource(currencyRateRepository.getRates()) { value ->
            if (value.status == Resource.Status.SUCCESS) {

                val timestamp = value.data?.timestamp as Long

                val listBasicCurrencyRate: List<BasicCurrencyRate> =
                    value.data.rates.map { currencyRate ->
                        BasicCurrencyRate(
                            currencyRate.basicCurrencyRate.currencyShortName,
                            currencyRate.basicCurrencyRate.rate,
                        )
                    }

                val listWithResources = currencyDecoratorService.convert(listBasicCurrencyRate)
                val decoratedData = RatesData(timestamp, listWithResources)

                latestNetworkState = decoratedData

                // Save data to Room
                launch {
                    withContext(Dispatchers.IO) {
                        val list = decoratedData.rates
                        if (list.isNotEmpty()) {
                            for (item in list) {
                                database.insert(
                                    Rate(
                                        code = item.basicCurrencyRate.currencyShortName,
                                        rate = item.basicCurrencyRate.rate.toDouble()
                                    )
                                )
                            }
                            sharedPreferenceManager.ratesCachedLocally = true
                        }
                    }
                }

                // Do rate calculations on background worker thread
                launch {
                    withContext(Dispatchers.IO) {
                        updatedRates.postValue(
                            updateRates(
                                selectedAmount,
                                selectedCurrency,
                                latestNetworkState
                            )
                        )
                    }
                }

            } else {
                updatedRates.value = value
            }
        }

        // Manual updates
        updatedRates.addSource(manuallyEnteredValues) { value ->
            updatedRates.value = value
        }
        return updatedRates
    }

    fun enterCurrencyValue(selectedAmount: BigDecimal, selectedCurrency: String) {
        this.selectedAmount = selectedAmount
        this.selectedCurrency = selectedCurrency

        launch {
            withContext(Dispatchers.IO) {
                manuallyEnteredValues.postValue(
                    updateRates(
                        selectedAmount,
                        selectedCurrency,
                        latestNetworkState
                    )
                )
            }
        }
    }

    private fun updateRates(
        selectedAmount: BigDecimal?,
        selectedCurrencyShortName: String?,
        latestNetworkState: RatesData?
    ): Resource<RatesData> {
        if (latestNetworkState == null) {
            throw IllegalStateException("Network error while converting currency rates")
        }

        if (selectedCurrencyShortName == null || selectedAmount == null) {
            return Resource.success(latestNetworkState)
        } else {
            val oldValue =
                latestNetworkState.rates.first { currencyRate ->
                    currencyRate.basicCurrencyRate.currencyShortName == selectedCurrencyShortName
                }.basicCurrencyRate.rate

            val coefficient = selectedAmount.divide(oldValue, SCALE, RoundingMode.HALF_UP)
            val updatedRates = latestNetworkState.rates.map { currencyRate ->
                val newRate =
                    if (currencyRate.basicCurrencyRate.currencyShortName == selectedCurrencyShortName) selectedAmount
                    else (currencyRate.basicCurrencyRate.rate * coefficient)
                currencyRate.copy(basicCurrencyRate = currencyRate.basicCurrencyRate.copy(rate = newRate))
            }.sortedByDescending { currencyRate ->
                if (currencyRate.basicCurrencyRate.currencyShortName == selectedCurrencyShortName) 1 else 0
            }
            return Resource.success(latestNetworkState.copy(rates = updatedRates))
        }
    }
}
