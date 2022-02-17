package com.sample.scoped_storage.root.ui

import com.arkivanov.decompose.router.RouterState
import com.sample.scoped_storage.modules.app_storage.ui.AppStorageComponent
import com.sample.scoped_storage.modules.file_picker.ui.FilePickerComponent
import com.sample.scoped_storage.modules.media_store.ui.MediaStoreComponent
import com.sample.scoped_storage.navigation.ui.NavigationComponent

interface RootComponent {

    val routerState: RouterState<*, Child>

    sealed interface Child {
        class Navigation(val component: NavigationComponent) : Child
        class AppStorage(val component: AppStorageComponent) : Child
        class MediaStore(val component: MediaStoreComponent) : Child
        class FilePicker(val component: FilePickerComponent) : Child
    }
}