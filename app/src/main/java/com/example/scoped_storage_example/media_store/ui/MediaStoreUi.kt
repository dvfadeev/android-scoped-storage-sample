package com.example.scoped_storage_example.media_store.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.fetch.VideoFrameUriFetcher
import coil.request.videoFrameMillis
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
                component = component,
                modifier = Modifier.padding(it),
                onLoadMedia = component::onLoadMedia
            )
        }
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun MediaStorePermissionScreen(
    component: MediaStoreComponent,
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

        component.mediaFiles?.let {
            MediaStoreContent(
                mediaFiles = it,
                modifier = modifier
            )
        } ?: run {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun MediaStoreContent(
    mediaFiles: List<MediaFileViewData>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(mediaFiles) {
            MediaFileItem(data = it)
        }
    }
}

@Composable
private fun MediaFileItem(
    data: MediaFileViewData,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(modifier = modifier.fillMaxWidth()) {

        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.surface, RoundedCornerShape(8.dp)),
            painter = rememberImagePainter(
                data.uri,
                builder = {
                    with(LocalDensity.current) { size(60.dp.roundToPx()) }
                    if(data.type == "video") {
                        fetcher(VideoFrameUriFetcher(context))
                        crossfade(true)
                    }
                }
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Text(
            text = data.name,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = data.date
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MediaStoreUiPreview() {
    AppTheme {
        MediaStoreUi(FakeMediaStoreComponent())
    }
}

class FakeMediaStoreComponent : MediaStoreComponent {

    override var mediaFiles: List<MediaFileViewData>? = List(5) {
        MediaFileViewData.MOCK
    }

    override fun onLoadMedia() = Unit
}

