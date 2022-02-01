package com.example.scoped_storage_example.root.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.example.scoped_storage_example.app_storage.createAppStorageComponent
import com.example.scoped_storage_example.core.data.gateway.logger.Logger
import com.example.scoped_storage_example.core.utils.ComponentFactory
import com.example.scoped_storage_example.core.utils.toComposeState
import com.example.scoped_storage_example.file_picker.createFilePickerComponent
import com.example.scoped_storage_example.media_store.createMediaStoreComponent
import com.example.scoped_storage_example.navigation.createNavigationComponent
import com.example.scoped_storage_example.navigation.ui.NavigationComponent
import com.example.scoped_storage_example.navigation.ui.NavigationModule
import kotlinx.parcelize.Parcelize

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val logger: Logger
) : ComponentContext by componentContext, RootComponent {

    private val router = router<ChildConfig, RootComponent.Child>(
        initialConfiguration = ChildConfig.Navigation,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val routerState: RouterState<*, RootComponent.Child> by router.state.toComposeState(
        lifecycle
    )

    private fun createChild(config: ChildConfig, componentContext: ComponentContext): RootComponent.Child {
        logger.log("RootComponent navigate to ${config::class.simpleName}")
        return when (config) {
            is ChildConfig.Navigation -> RootComponent.Child.Navigation(
                componentFactory.createNavigationComponent(componentContext, ::onNavigationOutput)
            )
            is ChildConfig.AppStorage -> RootComponent.Child.AppStorage(
                componentFactory.createAppStorageComponent(componentContext)
            )
            is ChildConfig.MediaStore -> RootComponent.Child.MediaStore(
                componentFactory.createMediaStoreComponent(componentContext)
            )
            is ChildConfig.FilePicker -> RootComponent.Child.FilePicker(
                componentFactory.createFilePickerComponent(componentContext)
            )
        }
    }

    private fun onNavigationOutput(output: NavigationComponent.Output) {
        when (output) {
            is NavigationComponent.Output.ModuleRequested -> {
                when (output.type) {
                    NavigationModule.AppStorage -> {
                        router.push(ChildConfig.AppStorage)
                    }
                    NavigationModule.MediaStore -> {
                        router.push(ChildConfig.MediaStore)
                    }
                    NavigationModule.FilePicker -> {
                        router.push(ChildConfig.FilePicker)
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

        @Parcelize
        object MediaStore : ChildConfig

        @Parcelize
        object FilePicker : ChildConfig
    }
}