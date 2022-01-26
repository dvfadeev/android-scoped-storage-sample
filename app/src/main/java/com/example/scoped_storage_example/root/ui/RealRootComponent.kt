package com.example.scoped_storage_example.root.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.example.scoped_storage_example.core.ui.ComponentFactory
import com.example.scoped_storage_example.core.utils.toComposeState
import com.example.scoped_storage_example.navigation.createNavigationComponent
import kotlinx.parcelize.Parcelize


class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, RootComponent {

    private val router = router<ChildConfig, RootComponent.Child>(
        initialConfiguration = ChildConfig.Navigation,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: RouterState<*, RootComponent.Child> by router.state.toComposeState(
        lifecycle
    )

    private fun createChild(config: ChildConfig, componentContext: ComponentContext) =
        when (config) {
            is ChildConfig.Navigation -> RootComponent.Child.Navigation(
                componentFactory.createNavigationComponent(componentContext)
            )
        }

    private sealed interface ChildConfig : Parcelable {

        @Parcelize
        object Navigation : ChildConfig
    }
}