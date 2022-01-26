package com.example.scoped_storage_example.app_storage

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.app_storage.ui.AppStorageComponent
import com.example.scoped_storage_example.app_storage.ui.RealAppStorageComponent
import com.example.scoped_storage_example.core.utils.ComponentFactory
import org.koin.dsl.module

val appStorageModule = module {
}

fun ComponentFactory.createAppStorageComponent(
    componentContext: ComponentContext
): AppStorageComponent {
    return RealAppStorageComponent(componentContext)
}