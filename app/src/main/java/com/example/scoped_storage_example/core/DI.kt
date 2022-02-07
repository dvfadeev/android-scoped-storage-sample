package com.example.scoped_storage_example.core

import com.example.scoped_storage_example.core.data.CurrentTime
import com.example.scoped_storage_example.core.data.CurrentTimeImpl
import com.example.scoped_storage_example.core.data.Logger
import com.example.scoped_storage_example.core.data.LoggerImpl
import org.koin.dsl.module

val coreModule = module {
    single<Logger> { LoggerImpl(get()) }
    single<CurrentTime> { CurrentTimeImpl() }
}