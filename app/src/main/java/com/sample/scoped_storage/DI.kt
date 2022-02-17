package com.sample.scoped_storage

import com.sample.scoped_storage.core.coreModule
import com.sample.scoped_storage.modules.app_storage.appStorageModule
import com.sample.scoped_storage.modules.file_picker.filePickerModule
import com.sample.scoped_storage.modules.media_store.mediaStoreModule
import com.sample.scoped_storage.navigation.navigationModule
import com.sample.scoped_storage.root.rootModule

val allModules = listOf(
    coreModule,
    rootModule,
    navigationModule,
    appStorageModule,
    mediaStoreModule,
    filePickerModule
)