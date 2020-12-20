package com.cst.currencyconverter.ui.rates.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.cst.currencyconverter.data.CurrencyRate

class CurrencyRateDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem.basicCurrencyRate.currencyShortName == newItem.basicCurrencyRate.currencyShortName
    }

    override fun areContentsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem == newItem
    }
}