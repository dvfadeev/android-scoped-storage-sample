package com.example.scoped_storage_example.core

import com.example.scoped_storage_example.core.data.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single<ComponentToast> { ComponentToastImpl(androidContext()) }
    single<Logger> { LoggerImpl(get()) }
    single<CurrentTime> { CurrentTimeImpl() }
}