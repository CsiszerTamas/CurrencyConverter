package com.cst.currencyconverter.utils

import com.cst.currencyconverter.data.CurrencyRate
import com.cst.currencyconverter.data.model.BasicCurrencyRate

class CurrencyItemDecoratorService(
    currencyShortNames: Array<String>,
    private val descriptions: Array<String>,
    private val iconProviderService: IconProviderService
) {
    private val descriptionMap: MutableMap<String, String> = HashMap()

    init {
        currencyShortNames.forEachIndexed { index, currencyShortName ->
            descriptionMap += currencyShortName to descriptions[index]
        }
    }

    fun convert(inputList: List<BasicCurrencyRate>): List<CurrencyRate> {
        return inputList.map { basicCurrencyRate ->
            CurrencyRate(
                basicCurrencyRate,
                descriptionMap[basicCurrencyRate.currencyShortName] ?: "",
                iconProviderService.loadIconResource(basicCurrencyRate.currencyShortName)
            )
        }
    }
}
