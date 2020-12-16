package com.cst.currencyconverter

import android.app.Application
import com.cst.currencyconverter.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CurrencyConverterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // using Koin library for dependency injection
        startKoin {
            androidContext(this@CurrencyConverterApplication)
            modules(appModules)
        }

        // Enabling Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
