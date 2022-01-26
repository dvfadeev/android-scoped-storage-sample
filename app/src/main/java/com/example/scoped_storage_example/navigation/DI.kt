package com.example.scoped_storage_example.navigation

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.ui.ComponentFactory
import com.example.scoped_storage_example.navigation.ui.NavigationComponent
import com.example.scoped_storage_example.navigation.ui.RealNavigationComponent
import org.koin.dsl.module

val navigationModule = module {
}

fun ComponentFactory.createNavigationComponent(
    componentContext: ComponentContext,
    onOutput: (NavigationComponent.Output) -> Unit
): NavigationComponent {
    return RealNavigationComponent(componentContext, onOutput)
}