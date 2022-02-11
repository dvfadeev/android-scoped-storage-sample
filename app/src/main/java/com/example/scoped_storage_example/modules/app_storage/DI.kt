package com.example.scoped_storage_example.modules.app_storage

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.modules.app_storage.data.AppStorageGatewayExternal
import com.example.scoped_storage_example.modules.app_storage.data.AppStorageGatewayInternal
import com.example.scoped_storage_example.modules.app_storage.ui.AppStorageComponent
import com.example.scoped_storage_example.modules.app_storage.ui.RealAppStorageComponent
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