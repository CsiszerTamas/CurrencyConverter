package com.cst.currencyconverter.data.model

data class BasicRatesData(
    val timestamp: Long,
    val rates: List<BasicCurrencyRate>?
)
