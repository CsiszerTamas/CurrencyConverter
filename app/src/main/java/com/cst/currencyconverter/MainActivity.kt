package com.cst.currencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.cst.currencyconverter.constants.Constants
import com.cst.currencyconverter.databinding.ActivityMainBinding
import com.cst.currencyconverter.utils.SharedPreferenceManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val sharedPreferenceManager: SharedPreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        configureAppTheme()
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    fun configureAppTheme() {
        when (sharedPreferenceManager.nightModeCode) {
            Constants.LIGHT_MODE -> {
                // Only LIGHT mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.Theme_CurrencyConverter_TransparentLightTheme)
            }
            Constants.DARK_MODE -> {
                // Only NIGHT mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.Theme_CurrencyConverter_TransparentDarkTheme)
            }
            else -> {
            }
        }
    }
}
