package com.cst.currencyconverter.data.network

import androidx.lifecycle.LiveData
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.data.model.BasicRatesData

internal interface BasicCurrencyDataSource {
    fun getRates(): LiveData<Resource<BasicRatesData>>
}
