package com.cst.currencyconverter.ui.rates.diffutil

import android.os.Bundle
import com.cst.currencyconverter.data.CurrencyRate

class RatesDiffUtilCallback(
    private val newRates: List<CurrencyRate>,
    private val oldRates: List<CurrencyRate>
) : DiffUtilsCallback<CurrencyRate>(
    oldRates, newRates,
    { rate1, rate2 -> rate1.basicCurrencyRate.currencyShortName == rate2.basicCurrencyRate.currencyShortName },
    { rate1, rate2 -> rate1 == rate2 }) {

    companion object {
        const val KEY_FLAG = "FLAG"
        const val KEY_TICKER = "TICKER"
        const val KEY_DESCRIPTION = "DESCRIPTION"
        const val KEY_AMOUNT = "AMOUNT"
    }

    // This method is responsible for changing the items in the list
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newRate: CurrencyRate = newRates[newItemPosition]
        val oldRate: CurrencyRate = oldRates[oldItemPosition]

        val diffBundle = Bundle()

        // We add to the diffBundle() the UI elements which are needed to change
        if (newRate.basicCurrencyRate.rate != oldRate.basicCurrencyRate.rate) {
            diffBundle.putString(KEY_AMOUNT, newRate.basicCurrencyRate.rate.toPlainString())
        }
        if (newRate.basicCurrencyRate.currencyShortName != oldRate.basicCurrencyRate.currencyShortName) {
            diffBundle.putString(KEY_TICKER, newRate.basicCurrencyRate.currencyShortName)
        }
        if (newRate.iconId != oldRate.iconId) {
            diffBundle.putInt(KEY_FLAG, newRate.iconId)
        }
        if (newRate.currencyDescription != oldRate.currencyDescription) {
            diffBundle.putString(KEY_DESCRIPTION, newRate.currencyDescription)
        }
        return if (diffBundle.size() == 0) null else diffBundle
    }
}
