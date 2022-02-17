package com.sample.scoped_storage.modules.app_storage

import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.modules.app_storage.data.AppStorageGatewayExternal
import com.sample.scoped_storage.modules.app_storage.data.AppStorageGatewayInternal
import com.sample.scoped_storage.modules.app_storage.ui.AppStorageComponent
import com.sample.scoped_storage.modules.app_storage.ui.RealAppStorageComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val appStorageModule = module {
    single { AppStorageGatewayInternal(androidContext()) }
    single { AppStorageGatewayExternal(androidContext()) }
}

fun ComponentFactory.createAppStorageComponent(
    componentContext: ComponentContext
): AppStorageComponent {
    return RealAppStorageComponent(componentContext, get(), get(), get(), get())
}