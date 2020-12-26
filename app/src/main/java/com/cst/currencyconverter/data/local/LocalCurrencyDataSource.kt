package com.cst.currencyconverter.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.data.model.BasicCurrencyRate
import com.cst.currencyconverter.data.model.BasicRatesData
import com.cst.currencyconverter.data.network.BasicCurrencyDataSource
import com.cst.currencyconverter.utils.SharedPreferenceManager
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

class LocalCurrencyDataSource(
    private val database: CurrencyDao,
    private val sharedPreferenceManager: SharedPreferenceManager,
) : BasicCurrencyDataSource, KoinComponent, CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    fun setBasicRatesData(basicRatesData: BasicRatesData) {
        sharedPreferenceManager.cachedRatesTimestamp = basicRatesData.timestamp
    }

    override fun getRates(): LiveData<Resource<BasicRatesData>> {

        val liveData = MutableLiveData<Resource<BasicRatesData>>()

        launch {
            withContext(Dispatchers.IO) {
                val list = database.getAllRates()
                liveData.postValue(
                    Resource.success(
                        BasicRatesData(
                            sharedPreferenceManager.cachedRatesTimestamp,
                            mapLocalDbRatesToRates(list)
                        )
                    )
                )
            }
        }
        return liveData
    }

    private fun mapLocalDbRatesToRates(rates: List<Rate>): List<BasicCurrencyRate> {
        return rates.map { currencyRate ->
            BasicCurrencyRate(
                currencyRate.code,
                BigDecimal.valueOf(currencyRate.rate)
            )
        }
    }
}
