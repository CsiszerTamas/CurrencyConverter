package com.cst.currencyconverter.data.repositories.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.cst.currencyconverter.constants.Constants
import com.cst.currencyconverter.data.CurrencyRate
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.data.local.LocalCurrencyDataSource
import com.cst.currencyconverter.data.model.BasicRatesData
import com.cst.currencyconverter.data.model.RatesData
import com.cst.currencyconverter.data.network.NetworkCurrencyDataSource

class CurrencyRateRepositoryImpl(
    private val networkCurrencyDataSource: NetworkCurrencyDataSource,
    private val localCurrencyDataSource: LocalCurrencyDataSource,
    currencyShortNames: Array<String>,
    descriptions: Array<String>
) : CurrencyRateRepository {
    private val descriptionMap: MutableMap<String, String> = HashMap()

    init {
        currencyShortNames.forEachIndexed { index, currencyShortName ->
            descriptionMap += currencyShortName to descriptions[index]
        }
    }

    override fun getRates(): LiveData<Resource<RatesData>> {

        // With using the MediatorLiveData we combine the 2 sources of the data
        val mediatorLiveData = MediatorLiveData<Resource<BasicRatesData>>()
        var successDataReceived = false

        // Add network data source
        mediatorLiveData.addSource(networkCurrencyDataSource.getRates()) { value ->
            if (value.status == Resource.Status.SUCCESS) {
                successDataReceived = true
                value.data?.let { localCurrencyDataSource.setBasicRatesData(it) }
            }

            if (!successDataReceived || value.status != Resource.Status.LOADING) {
                // we don't want to send loading when we already sent data
                mediatorLiveData.value = value
            }
        }

        // Add local data source
        mediatorLiveData.addSource(localCurrencyDataSource.getRates()) { value ->
            if (value.status == Resource.Status.SUCCESS) {
                successDataReceived = true
            }
            mediatorLiveData.value = value
        }

        /* We use the map() method here to get back LiveData<Resource<RatesData>>
           from the MediatorLiveData<Resource<BasicRatesData>>() */
        return Transformations.map(mediatorLiveData) { resource ->
            when (resource.status) {
                Resource.Status.ERROR -> Resource.error(resource.message ?: "", null)
                Resource.Status.LOADING -> Resource.loading(null)
                Resource.Status.SUCCESS -> {
                    val data = resource.data!!
                    val currencyRateList = data.rates?.map { basicCurrencyRate ->
                        CurrencyRate(
                            basicCurrencyRate,
                            Constants.EMPTY_DESCRIPTION,
                            Constants.DEFAULT_ICON_ID
                        )
                    } ?: emptyList()

                    Resource.success(RatesData(data.timestamp, currencyRateList))
                }
            }
        }
    }
}
