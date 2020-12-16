package com.cst.currencyconverter.data.model

import com.cst.currencyconverter.data.CurrencyRate

data class RatesData(
    val timestamp: Long,
    val rates: List<CurrencyRate>
)
