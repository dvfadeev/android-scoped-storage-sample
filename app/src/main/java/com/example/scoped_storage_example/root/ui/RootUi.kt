package com.example.scoped_storage_example.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.router.RouterState
import com.example.scoped_storage_example.app_storage.ui.AppStorageUi
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.file_picker.ui.FilePickerUi
import com.example.scoped_storage_example.media_store.ui.MediaStoreUi
import com.example.scoped_storage_example.navigation.ui.FakeNavigationComponent
import com.example.scoped_storage_example.navigation.ui.NavigationUi

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
            Children(component.routerState) { child ->
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