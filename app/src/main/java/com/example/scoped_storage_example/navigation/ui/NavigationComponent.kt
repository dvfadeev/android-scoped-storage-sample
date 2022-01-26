package com.example.scoped_storage_example.navigation.ui

interface NavigationComponent {

    fun onAppStorageClick()

    sealed interface Output {
        class ModuleRequested(val type: NavigationModule) : Output
    }
}