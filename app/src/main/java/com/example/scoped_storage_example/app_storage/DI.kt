package com.example.scoped_storage_example.app_storage

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.app_storage.data.AppStorageGateway
import com.example.scoped_storage_example.app_storage.data.AppStorageGatewayImpl
import com.example.scoped_storage_example.app_storage.ui.AppStorageComponent
import com.example.scoped_storage_example.app_storage.ui.RealAppStorageComponent
import com.example.scoped_storage_example.core.utils.ComponentFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.module

val appStorageModule = module {
    single<AppStorageGateway> { AppStorageGatewayImpl(androidContext()) }
}

fun ComponentFactory.createAppStorageComponent(
    componentContext: ComponentContext
): AppStorageComponent {
    return RealAppStorageComponent(componentContext, get(), get())
}