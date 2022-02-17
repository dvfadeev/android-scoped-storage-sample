package com.sample.scoped_storage.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.slide
import com.arkivanov.decompose.router.RouterState
import com.sample.scoped_storage.core.ui.theme.AppTheme
import com.sample.scoped_storage.modules.app_storage.ui.AppStorageUi
import com.sample.scoped_storage.modules.file_picker.ui.FilePickerUi
import com.sample.scoped_storage.modules.media_store.ui.MediaStoreUi
import com.sample.scoped_storage.navigation.ui.FakeNavigationComponent
import com.sample.scoped_storage.navigation.ui.NavigationUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    AppTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = modifier
                .fillMaxSize()
        ) {
            Children(component.routerState, animation = slide()) { child ->
                when (val instance = child.instance) {
                    is RootComponent.Child.Navigation -> NavigationUi(component = instance.component)
                    is RootComponent.Child.AppStorage -> AppStorageUi(component = instance.component)
                    is RootComponent.Child.MediaStore -> MediaStoreUi(component = instance.component)
                    is RootComponent.Child.FilePicker -> FilePickerUi(component = instance.component)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}

class FakeRootComponent : RootComponent {

    override val routerState: RouterState<*, RootComponent.Child> = RouterState(
        activeChild = Child.Created(
            configuration = "<fake>",
            instance = RootComponent.Child.Navigation(FakeNavigationComponent())
        )
    )
}