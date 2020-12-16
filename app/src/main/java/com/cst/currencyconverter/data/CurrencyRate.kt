package com.cst.currencyconverter.data

import com.cst.currencyconverter.data.model.BasicCurrencyRate

data class CurrencyRate(
    val basicCurrencyRate: BasicCurrencyRate,
    val currencyDescription: String,
    val iconId: Int
)
