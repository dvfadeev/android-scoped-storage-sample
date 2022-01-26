package com.example.scoped_storage_example

import com.example.scoped_storage_example.app_storage.appStorageModule
import com.example.scoped_storage_example.navigation.navigationModule
import com.example.scoped_storage_example.root.rootModule

val allModules = listOf(
    rootModule,
    navigationModule,
    appStorageModule
)