package com.cst.currencyconverter.data.model

import java.math.BigDecimal

data class BasicCurrencyRate(
    val currencyShortName: String,
    val rate: BigDecimal
)
