package com.example.scoped_storage_example.media_store.ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
import com.example.scoped_storage_example.core.ui.widgets.Toolbar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MediaStoreUi(
    component: MediaStoreComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.media_store_title),
                backNavigationEnabled = true
            )
        },
        content = {
            MediaStorePermissionScreen(
                modifier = Modifier.padding(it),
                onLoadMedia = component::onLoadMedia
            )
        }
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun MediaStorePermissionScreen(
    modifier: Modifier = Modifier,
    onLoadMedia: () -> Unit
) {
    val context = LocalContext.current
    val storagePermissionState = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    PermissionRequired(
        permissionState = storagePermissionState,
        permissionNotGrantedContent = {
            Box(modifier = modifier.fillMaxSize()) {
                Column(modifier = modifier.align(Alignment.Center)) {
                    Text(
                        text = stringResource(id = R.string.media_store_permission_request),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(CenterHorizontally)
                    ) {
                        ControlButton(
                            text = stringResource(id = R.string.ok),
                            onClick = { storagePermissionState.launchPermissionRequest() }
                        )
                        ControlButton(
                            text = stringResource(id = R.string.close),
                            onClick = { (context as Activity).onBackPressed() }
                        )
                    }
                }
            }
        },
        permissionNotAvailableContent = {
        }
    ) {
        onLoadMedia.invoke()
        MediaStoreContent(modifier = modifier)
    }
}

@Composable
private fun MediaStoreContent(
    modifier: Modifier = Modifier
) {

}

@Preview(showSystemUi = true)
@Composable
private fun MediaStoreUiPreview() {
    AppTheme {
        MediaStoreUi(FakeMediaStoreComponent())
    }
}

class FakeMediaStoreComponent : MediaStoreComponent {

    override fun onLoadMedia() = Unit
}

