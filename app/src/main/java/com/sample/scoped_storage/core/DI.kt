package com.sample.scoped_storage.core

import com.sample.scoped_storage.core.data.*
import com.sample.scoped_storage.core.utils.ActivityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single(createdAtStart = true) { ActivityProvider(androidContext() as com.sample.scoped_storage.App) }
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }
    single<ComponentToast> { ComponentToastImpl(androidContext()) }
    single<Logger> { LoggerImpl(get()) }
    single<CurrentTime> { CurrentTimeImpl() }
    single<PermissionValidator> { PermissionValidatorImpl(get(), get()) }
}