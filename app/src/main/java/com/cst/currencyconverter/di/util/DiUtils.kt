package com.cst.currencyconverter.di.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

inline fun <T : ViewModel> LifecycleOwner.viewModel(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy { getViewModel<T>(clazz, qualifier, parameters) }
