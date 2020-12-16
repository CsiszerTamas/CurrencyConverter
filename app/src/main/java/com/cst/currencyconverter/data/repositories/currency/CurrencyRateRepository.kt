package com.cst.currencyconverter.data.repositories.currency

import androidx.lifecycle.LiveData
import com.cst.currencyconverter.data.Resource
import com.cst.currencyconverter.data.model.RatesData

interface CurrencyRateRepository {
    fun getRates(): LiveData<Resource<RatesData>>
}
