package com.example.scoped_storage_example

import com.example.scoped_storage_example.core.coreModule
import com.example.scoped_storage_example.modules.app_storage.appStorageModule
import com.example.scoped_storage_example.modules.file_picker.filePickerModule
import com.example.scoped_storage_example.modules.media_store.mediaStoreModule
import com.example.scoped_storage_example.navigation.navigationModule
import com.example.scoped_storage_example.root.rootModule

val allModules = listOf(
    coreModule,
    rootModule,
    navigationModule,
    appStorageModule,
    mediaStoreModule,
    filePickerModule
)