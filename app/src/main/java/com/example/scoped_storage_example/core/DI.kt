package com.example.scoped_storage_example.core

import com.example.scoped_storage_example.App
import com.example.scoped_storage_example.core.data.*
import com.example.scoped_storage_example.core.utils.ActivityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single(createdAtStart = true) { ActivityProvider(androidContext() as App) }
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }
    single<ComponentToast> { ComponentToastImpl(androidContext()) }
    single<Logger> { LoggerImpl(get()) }
    single<CurrentTime> { CurrentTimeImpl() }
    single<PermissionValidator> { PermissionValidatorImpl(get(), get()) }
}