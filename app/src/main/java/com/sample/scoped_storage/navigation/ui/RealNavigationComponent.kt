package com.sample.scoped_storage.navigation.ui

import com.arkivanov.decompose.ComponentContext

class RealNavigationComponent(
    componentContext: ComponentContext,
    private val onOutput: (NavigationComponent.Output) -> Unit
) : ComponentContext by componentContext, NavigationComponent {

    override fun onAppStorageClick() {
        openModule(NavigationModule.AppStorage)
    }

    override fun onMediaStoreClick() {
        openModule(NavigationModule.MediaStore)
    }

    override fun onFilePickerClick() {
        openModule(NavigationModule.FilePicker)
    }

    private fun openModule(module: NavigationModule) {
        onOutput(NavigationComponent.Output.ModuleRequested(module))

    }
}