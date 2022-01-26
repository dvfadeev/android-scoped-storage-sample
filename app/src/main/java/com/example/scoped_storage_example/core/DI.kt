package com.example.scoped_storage_example.core

import com.example.scoped_storage_example.core.data.current_time.CurrentTime
import com.example.scoped_storage_example.core.data.current_time.CurrentTimeImpl
import com.example.scoped_storage_example.core.data.logger.Logger
import com.example.scoped_storage_example.core.data.logger.LoggerImpl
import org.koin.dsl.module

val coreModule = module {
    single<Logger> { LoggerImpl(get()) }
    single<CurrentTime> { CurrentTimeImpl() }
}