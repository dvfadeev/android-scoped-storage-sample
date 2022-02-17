package com.sample.scoped_storage.root

import com.arkivanov.decompose.ComponentContext
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.root.ui.RealRootComponent
import com.sample.scoped_storage.root.ui.RootComponent
import org.koin.core.component.get
import org.koin.dsl.module

val rootModule = module {
}

fun ComponentFactory.createRootComponent(componentContext: ComponentContext): RootComponent {
    return RealRootComponent(componentContext, get(), get())
}
