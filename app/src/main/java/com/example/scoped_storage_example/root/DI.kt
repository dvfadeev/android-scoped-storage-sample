package com.example.scoped_storage_example.root

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.root.ui.RealRootComponent
import com.example.scoped_storage_example.root.ui.RootComponent
import org.koin.dsl.module
import org.koin.core.component.get

val rootModule = module {
}

fun ComponentFactory.createRootComponent(componentContext: ComponentContext): RootComponent {
    return RealRootComponent(componentContext, get())
}
