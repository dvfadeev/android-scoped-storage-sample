package com.example.scoped_storage_example.root.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.example.scoped_storage_example.app_storage.createAppStorageComponent
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.core.utils.toComposeState
import com.example.scoped_storage_example.navigation.createNavigationComponent
import com.example.scoped_storage_example.navigation.ui.NavigationComponent
import com.example.scoped_storage_example.navigation.ui.NavigationModule
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
                componentFactory.createNavigationComponent(componentContext, ::onNavigationOutput)
            )
            is ChildConfig.AppStorage -> RootComponent.Child.AppStorage(
                componentFactory.createAppStorageComponent(componentContext)
            )
        }

    private fun onNavigationOutput(output: NavigationComponent.Output) {
        when (output) {
            is NavigationComponent.Output.ModuleRequested -> {
                when (output.type) {
                    NavigationModule.AppStorage -> {
                        router.push(ChildConfig.AppStorage)
                    }
                }
            }
        }
    }

    private sealed interface ChildConfig : Parcelable {

        @Parcelize
        object Navigation : ChildConfig

        @Parcelize
        object AppStorage : ChildConfig
    }
}