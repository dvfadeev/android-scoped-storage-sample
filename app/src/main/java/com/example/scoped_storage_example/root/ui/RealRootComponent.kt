package com.example.scoped_storage_example.root.ui

import com.arkivanov.decompose.ComponentContext
import com.example.scoped_storage_example.core.ui.ComponentFactory

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, RootComponent {

}