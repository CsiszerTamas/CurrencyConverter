package com.cst.currencyconverter.utils

import android.content.SharedPreferences

class SharedPreferenceManager(private val sharedPreferences: SharedPreferences) {

    companion object {

        const val SHARED_PREFERENCES_IDENTIFIER_NAME = "RATES"

        // Long
        const val PREFERENCES_TIMESTAMP = "TIMESTAMP"

        // Boolean
        const val PREFERENCES_RATES_CACHED = "RATES_CACHED"
        const val PREFERENCES_SELECTED_CURRENCY_SAVED = "SELECTED_CURRENCY_SAVED"

        // Int
        const val PREFERENCES_NIGHT_MODE_CODE = "NIGHT_MODE_CODE"

        // String
        const val PREFERENCES_SELECTED_CURRENCY = "SELECTED_CURRENCY"
    }

    var cachedRatesTimestamp: Long
        get() = readLong(PREFERENCES_TIMESTAMP, 0)
        set(value) = writeLong(PREFERENCES_TIMESTAMP, value)

    var ratesCachedLocally: Boolean
        get() = readBoolean(PREFERENCES_RATES_CACHED, false)
        set(value) = writeBoolean(PREFERENCES_RATES_CACHED, value)

    var nightModeCode: Int
        get() = readInt(PREFERENCES_NIGHT_MODE_CODE, 1)
        set(value) = writeInt(PREFERENCES_NIGHT_MODE_CODE, value)

    var selectedCurrency: String
        get() = readString(PREFERENCES_SELECTED_CURRENCY, "EUR") as String
        set(value) = writeString(PREFERENCES_SELECTED_CURRENCY, value)

    var selectedCurrencySaved: Boolean
        get() = readBoolean(PREFERENCES_SELECTED_CURRENCY_SAVED, false)
        set(value) = writeBoolean(PREFERENCES_SELECTED_CURRENCY_SAVED, value)

    // SharedPreferenceManager utility methods
    private fun remove(key: String) = sharedPreferences.edit().remove(key).apply()

    private fun readString(key: String, defaultValue: String): String? =
        sharedPreferences.getString(key, defaultValue)

    private fun writeString(key: String, value: String) = sharedPreferences.edit()
        .putString(key, value)
        .apply()

    private fun readBoolean(key: String, defaultValue: Boolean) =
        sharedPreferences.getBoolean(key, defaultValue)

    private fun writeBoolean(key: String, value: Boolean) = sharedPreferences.edit()
        .putBoolean(key, value)
        .apply()

    private fun readInt(key: String, defaultValue: Int) =
        sharedPreferences.getInt(key, defaultValue)

    private fun writeInt(key: String, value: Int) = sharedPreferences.edit()
        .putInt(key, value)
        .apply()

    private fun readLong(key: String, defaultValue: Long) =
        sharedPreferences.getLong(key, defaultValue)

    private fun writeLong(key: String, value: Long) = sharedPreferences.edit()
        .putLong(key, value)
        .apply()

    fun clearAllSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}
