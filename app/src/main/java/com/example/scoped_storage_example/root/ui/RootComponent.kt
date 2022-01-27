package com.example.scoped_storage_example.root.ui

import com.arkivanov.decompose.router.RouterState
import com.example.scoped_storage_example.app_storage.ui.AppStorageComponent
import com.example.scoped_storage_example.media_store.ui.MediaStoreComponent
import com.example.scoped_storage_example.navigation.ui.NavigationComponent

interface RootComponent {

    val routerState: RouterState<*, Child>

    sealed interface Child {
        class Navigation(val component: NavigationComponent) : Child
        class AppStorage(val component: AppStorageComponent) : Child
        class MediaStore(val component: MediaStoreComponent) : Child
    }
}