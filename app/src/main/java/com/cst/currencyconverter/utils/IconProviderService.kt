package com.cst.currencyconverter.utils

import android.content.Context
import com.cst.currencyconverter.BuildConfig
import java.util.*

class IconProviderService(private val context: Context) {

    companion object {
        private const val DRAWABLE = "drawable"
    }

    fun loadIconResource(currencyShortName: String): Int {
        return context.resources.getIdentifier(
            currencyShortName.toLowerCase(Locale.getDefault()),
            DRAWABLE,
            BuildConfig.APPLICATION_ID
        )
    }
}
