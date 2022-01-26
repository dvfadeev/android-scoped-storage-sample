package com.example.scoped_storage_example.navigation.ui

import com.arkivanov.decompose.ComponentContext

class RealNavigationComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, NavigationComponent {
}