package com.cst.currencyconverter.data

import androidx.lifecycle.LiveData
import com.cst.currencyconverter.data.model.BasicRatesData

internal interface BasicCurrencyDataSource {
    fun getRates(): LiveData<Resource<BasicRatesData>>
}
