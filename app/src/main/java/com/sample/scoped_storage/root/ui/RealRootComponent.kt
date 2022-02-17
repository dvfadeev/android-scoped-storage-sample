package com.sample.scoped_storage.root.ui

import android.os.Parcelable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.sample.scoped_storage.core.data.Logger
import com.sample.scoped_storage.core.utils.ComponentFactory
import com.sample.scoped_storage.core.utils.toComposeState
import com.sample.scoped_storage.modules.app_storage.createAppStorageComponent
import com.sample.scoped_storage.modules.file_picker.createFilePickerComponent
import com.sample.scoped_storage.modules.media_store.createMediaStoreComponent
import com.sample.scoped_storage.modules.media_store.ui.MediaStoreComponent
import com.sample.scoped_storage.navigation.createNavigationComponent
import com.sample.scoped_storage.navigation.ui.NavigationComponent
import com.sample.scoped_storage.navigation.ui.NavigationModule
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
                componentFactory.createMediaStoreComponent(componentContext, ::onMediaStoreOutput)
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

    private fun onMediaStoreOutput(output: MediaStoreComponent.Output) {
        when (output) {
            is MediaStoreComponent.Output.NavigationRequested -> {
                router.pop()
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