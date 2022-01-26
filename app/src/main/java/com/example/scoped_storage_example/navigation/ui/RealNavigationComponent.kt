package com.example.scoped_storage_example.navigation.ui

import com.arkivanov.decompose.ComponentContext

class RealNavigationComponent(
    componentContext: ComponentContext,
    private val onOutput: (NavigationComponent.Output) -> Unit
) : ComponentContext by componentContext, NavigationComponent {

    override fun onAppStorageClick() {
        onOutput(NavigationComponent.Output.ModuleRequested(NavigationModule.AppStorage))
    }
}