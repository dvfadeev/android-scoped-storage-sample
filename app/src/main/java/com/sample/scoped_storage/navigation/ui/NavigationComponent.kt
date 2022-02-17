package com.sample.scoped_storage.navigation.ui

interface NavigationComponent {

    fun onAppStorageClick()

    fun onMediaStoreClick()

    fun onFilePickerClick()

    sealed interface Output {
        class ModuleRequested(val type: NavigationModule) : Output
    }
}