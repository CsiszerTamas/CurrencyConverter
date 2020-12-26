package com.cst.currencyconverter.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.cst.currencyconverter.R
import com.cst.currencyconverter.constants.Constants
import com.cst.currencyconverter.data.local.CurrencyDao
import com.cst.currencyconverter.data.local.CurrencyDatabase
import com.cst.currencyconverter.data.local.LocalCurrencyDataSource
import com.cst.currencyconverter.data.network.NetworkCurrencyDataSource
import com.cst.currencyconverter.data.network.RatesNetworkService
import com.cst.currencyconverter.data.repositories.currency.CurrencyRateRepository
import com.cst.currencyconverter.data.repositories.currency.CurrencyRateRepositoryImpl
import com.cst.currencyconverter.ui.base.BaseViewModel
import com.cst.currencyconverter.ui.rates.RatesViewModel
import com.cst.currencyconverter.utils.CurrencyItemDecoratorService
import com.cst.currencyconverter.utils.IconProviderService
import com.cst.currencyconverter.utils.SharedPreferenceManager
import com.cst.currencyconverter.utils.SharedPreferenceManager.Companion.SHARED_PREFERENCES_IDENTIFIER_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModules = module {

    // Singles
    single { provideSharedPreferences(androidApplication()) }
    single { provideRoomDatabase(androidApplication()) }

    single { provideNetworkBasicCurrencyDataSource() }
    single {
        provideLocalCurrencyDataSource(
            database = get(),
            sharedPreferenceManager = get()
        )
    }
    single { provideIconDataSource(androidApplication()) }

    single(named(Constants.DI_CURRENCY_SHORT_NAME)) { provideCurrencyShortNames(androidApplication()) }
    single(named(Constants.DI_CURRENCY_DESCRIPTION)) {
        provideCurrencyDescriptions(
            androidApplication()
        )
    }

    // Factories
    factory { SharedPreferenceManager(sharedPreferences = get()) }

    factory<CurrencyRateRepository> {
        CurrencyRateRepositoryImpl(
            networkCurrencyDataSource = get(),
            localCurrencyDataSource = get(),
            currencyShortNames = get(named(Constants.DI_CURRENCY_SHORT_NAME)),
            descriptions = get(named(Constants.DI_CURRENCY_DESCRIPTION))
        )
    }

    factory {
        CurrencyItemDecoratorService(
            currencyShortNames = get(named(Constants.DI_CURRENCY_SHORT_NAME)),
            descriptions = get(named(Constants.DI_CURRENCY_DESCRIPTION)),
            iconProviderService = get()
        )
    }

    // ViewModels
    viewModel { BaseViewModel() }

    viewModel {
        RatesViewModel(
            database = get(),
            sharedPreferenceManager = get(),
            currencyRateRepository = get(),
            currencyDecoratorService = get()
        )
    }
}

// Provider methods
fun provideNetworkBasicCurrencyDataSource(): NetworkCurrencyDataSource {
    return NetworkCurrencyDataSource(
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RatesNetworkService::class.java)
    )
}

fun provideLocalCurrencyDataSource(
    database: CurrencyDao,
    sharedPreferenceManager: SharedPreferenceManager
): LocalCurrencyDataSource {

    return LocalCurrencyDataSource(
        database,
        sharedPreferenceManager
    )
}

fun provideIconDataSource(androidApplicationContext: Application): IconProviderService {
    return IconProviderService(androidApplicationContext)
}

fun provideCurrencyShortNames(androidApplicationContext: Application): Array<String> {
    return androidApplicationContext.resources.getStringArray(R.array.tickers)
}

fun provideCurrencyDescriptions(androidApplicationContext: Application): Array<String> {
    return androidApplicationContext.resources.getStringArray(R.array.currency_names)
}

fun provideSharedPreferences(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences(
        SHARED_PREFERENCES_IDENTIFIER_NAME,
        Context.MODE_PRIVATE
    )
}

fun provideRoomDatabase(androidApplication: Application): CurrencyDao {
    return CurrencyDatabase.getInstance(androidApplication).currencyDao
}
