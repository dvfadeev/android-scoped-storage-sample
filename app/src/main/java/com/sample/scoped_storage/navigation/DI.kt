package com.sample.scoped_storage.navigation

import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.navigation.ui.NavigationComponent
import com.sample.scoped_storage.navigation.ui.RealNavigationComponent
import org.koin.dsl.module

val navigationModule = module {
}

fun ComponentFactory.createNavigationComponent(
    componentContext: ComponentContext,
    onOutput: (NavigationComponent.Output) -> Unit
): NavigationComponent {
    return RealNavigationComponent(componentContext, onOutput)
}