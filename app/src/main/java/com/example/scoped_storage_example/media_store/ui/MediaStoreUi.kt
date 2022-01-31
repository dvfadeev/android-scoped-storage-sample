package com.example.scoped_storage_example.media_store.ui

import android.app.Activity
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.theme.additionalColors
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
import com.example.scoped_storage_example.core.ui.widgets.SelectableButton
import com.example.scoped_storage_example.core.ui.widgets.Toolbar
import com.example.scoped_storage_example.core.ui.widgets.verticalScrollbar
import com.example.scoped_storage_example.media_store.data.MediaType
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
                Column(modifier = modifier.align(Center)) {
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

        MediaStoreContent(
            mediaType = component.mediaType,
            mediaFiles = component.mediaFiles,
            onSaveBitmap = component::onSaveBitmap,
            onChangeMediaType = component::onChangeMediaType,
            modifier = modifier
        )
    }
}

@Composable
private fun MediaStoreContent(
    mediaType: MediaType,
    mediaFiles: List<MediaFileViewData>?,
    onSaveBitmap: (Bitmap) -> Unit,
    onChangeMediaType: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo ->
        photo?.let {
            onSaveBitmap(it)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        ControlButton(
            text = stringResource(id = R.string.media_store_take_photo),
            onClick = { launcher.launch() },
            modifier = Modifier.align(CenterHorizontally)
        )

        MediaTypeSelector(
            mediaType = mediaType,
            onChangeMediaType = {
                onChangeMediaType(it)
            },
            modifier = Modifier.align(CenterHorizontally)
        )

        mediaFiles?.let {
            val listState = rememberLazyListState()
            LazyColumn(
                contentPadding = PaddingValues(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .verticalScrollbar(listState, 4.dp, color = MaterialTheme.colors.primary),
                state = listState
            ) {
                items(mediaFiles) {
                    MediaFileItem(data = it)
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Center))
            }
        }
    }
}


@Composable
private fun MediaTypeSelector(
    mediaType: MediaType,
    onChangeMediaType: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        SelectableButton(
            text = stringResource(id = R.string.media_store_type_all),
            isSelected = mediaType == MediaType.All,
            onClick = { onChangeMediaType(MediaType.All) },
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
        )

        SelectableButton(
            text = stringResource(id = R.string.media_store_type_images),
            isSelected = mediaType == MediaType.Images,
            onClick = { onChangeMediaType(MediaType.Images) }
        )

        SelectableButton(
            text = stringResource(id = R.string.media_store_type_videos),
            isSelected = mediaType == MediaType.Videos,
            onClick = { onChangeMediaType(MediaType.Videos) }
        )

        SelectableButton(
            text = stringResource(id = R.string.media_store_type_audio),
            isSelected = mediaType == MediaType.Audio,
            onClick = { onChangeMediaType(MediaType.Audio) },
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
        )
    }
}

@Composable
private fun MediaFileItem(
    data: MediaFileViewData,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberImagePainter(
                data.uri,
                builder = {
                    with(LocalDensity.current) { size(72.dp.roundToPx()) }
                    placeholder(R.color.cardview_dark_background)
                    if (data.type == "video") {
                        decoder(VideoFrameDecoder(context))
                        crossfade(true)
                    }
                }
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = data.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            CaptionText(text = data.type + " " + stringResource(id = R.string.media_store_file))

            CaptionText(text = data.size)

            CaptionText(text = data.date)

            Divider(
                color = MaterialTheme.colors.onBackground,
                thickness = 2.dp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .alpha(0.1f)
            )
        }
    }
}

@Composable
fun CaptionText(
    text: String
) {
    Text(
        text = text,
        color = MaterialTheme.additionalColors.lightText,
        style = MaterialTheme.typography.caption
    )
}

@Preview(showSystemUi = true)
@Composable
private fun MediaStoreUiPreview() {
    AppTheme {
        val component = FakeMediaStoreComponent()
        MediaStoreContent(
            component.mediaType,
            component.mediaFiles!!,
            component::onSaveBitmap,
            component::onChangeMediaType
        )
    }
}

class FakeMediaStoreComponent : MediaStoreComponent {

    override var mediaType: MediaType = MediaType.All

    override var mediaFiles: List<MediaFileViewData>? = List(5) {
        MediaFileViewData.MOCK
    }

    override fun onLoadMedia() = Unit

    override fun onSaveBitmap(bitmap: Bitmap) = Unit

    override fun onChangeMediaType(mediaType: MediaType) = Unit
}

