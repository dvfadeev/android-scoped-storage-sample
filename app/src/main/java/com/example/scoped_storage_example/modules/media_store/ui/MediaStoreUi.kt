package com.example.scoped_storage_example.modules.media_store.ui

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.theme.additionalColors
import com.example.scoped_storage_example.core.ui.widgets.*
import com.example.scoped_storage_example.core.utils.AvailableFilters
import com.example.scoped_storage_example.core.utils.TypeFilter
import com.google.accompanist.permissions.ExperimentalPermissionsApi

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
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo ->
        photo?.let {
            component.onSaveBitmap(it)
        }
    }

    if (component.isCameraRequested) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            LaunchedEffect(key1 = Unit) {
                cameraLauncher.launch()
                component.onResetCameraRequest()
            }
        } else {
            PermissionChecker(
                permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                message = stringResource(id = R.string.media_store_write_permission_request),
                onCancelClick = { component.onResetCameraRequest() }) {
                LaunchedEffect(key1 = Unit) {
                    cameraLauncher.launch()
                    component.onResetCameraRequest()
                }
            }
        }
    } else {
        PermissionChecker(
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE,
            message = stringResource(id = R.string.media_store_read_permission_request),
            onCancelClick = {
                (context as Activity).onBackPressed()
            }
        ) {
            LaunchedEffect(key1 = Unit) {
                onLoadMedia.invoke()
            }

            val scrollState = rememberLazyListState()

            SlideAnimationScreen(
                firstScreen = {
                    MediaStoreContent(
                        filter = component.filter,
                        mediaFiles = component.mediaFiles,
                        onCameraRequest = component::onCameraRequest,
                        onChangeMediaType = component::onChangeFilter,
                        onFileClick = component::onFileClick,
                        onFileRemoveClick = component::onFileRemoveClick,
                        scrollState = scrollState,
                        modifier = modifier
                    )
                },
                secondScreen = {
                    component.selectedMediaFIle?.let {
                        FileContent(file = it)
                    } ?: run {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Center))
                        }
                    }
                },
                isShowSecondScreen = component.isShowImageFileContent
            )
        }
    }
}

@Composable
private fun MediaStoreContent(
    filter: TypeFilter,
    mediaFiles: List<MediaFileViewData>?,
    onCameraRequest: () -> Unit,
    onChangeMediaType: (TypeFilter) -> Unit,
    onFileClick: (Uri) -> Unit,
    onFileRemoveClick: (Uri) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        ControlButton(
            text = stringResource(id = R.string.media_store_take_photo),
            onClick = onCameraRequest,
            modifier = Modifier.align(CenterHorizontally)
        )

        MediaTypeSelector(
            filter = filter,
            onChangeFilter = {
                onChangeMediaType(it)
            },
            modifier = Modifier.align(CenterHorizontally)
        )

        mediaFiles?.let {
            LazyColumn(
                contentPadding = PaddingValues(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .verticalScrollbar(scrollState, 4.dp, color = MaterialTheme.colors.primary),
                state = scrollState
            ) {
                items(mediaFiles) {
                    MediaFileItem(
                        data = it,
                        onFileClick = onFileClick,
                        onFileRemoveClick = onFileRemoveClick
                    )
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
private fun FileContent(
    modifier: Modifier = Modifier,
    file: DetailedImageFileViewData
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 7.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                FileField(title = stringResource(id = R.string.media_store_file_name), text = file.name)
                FileField(title = stringResource(id = R.string.media_store_file_title), text = file.title)
                FileField(title = stringResource(id = R.string.media_store_file_path), text = file.path)
                FileField(title = stringResource(id = R.string.media_store_file_mime_type), text = file.mimeType)
                FileField(title = stringResource(id = R.string.media_store_file_size), text = file.size)
                FileField(title = stringResource(id = R.string.media_store_file_date_added), text = file.dateAdded)
                FileField(title = stringResource(id = R.string.media_store_file_date_taken), text = file.dateTaken)
                FileField(title = stringResource(id = R.string.media_store_file_description), text = file.description)

                file.resolution?.let {
                    FileField(title = stringResource(id = R.string.media_store_file_resolution), text = it)
                }

                file.duration?.let {
                    FileField(title = stringResource(id = R.string.media_store_file_duration), text = it)
                }
            }
        }

        val conf = LocalConfiguration.current
        val size = conf.screenWidthDp.dp - 64.dp

        ImageViewer(
            uri = file.uri,
            size = size,
            type = file.mimeType,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .align(CenterHorizontally)
        )
    }
}

@Composable
private fun MediaTypeSelector(
    filter: TypeFilter,
    onChangeFilter: (TypeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        AvailableFilters.list.forEach {

            val shape = when (it) {
                AvailableFilters.list.first() -> {
                    RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                }
                AvailableFilters.list.last() -> {
                    RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                }
                else -> {
                    RoundedCornerShape(0.dp)
                }
            }

            SelectableButton(
                text = stringResource(id = it.resource),
                isSelected = filter == it,
                onClick = { onChangeFilter(it) },
                shape = shape
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaFileItem(
    data: MediaFileViewData,
    onFileClick: (Uri) -> Unit,
    onFileRemoveClick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        data.uri?.let {
                            onFileClick(it)
                        }
                    },
                    onLongClick = { expanded = true }
                )
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
        ) {
            ImageViewer(
                uri = data.uri,
                size = 72.dp,
                type = data.type
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    data.uri?.let {
                        onFileRemoveClick(it)
                    }
                    expanded = false
                }
            ) {
                Text(stringResource(id = R.string.media_store_file_remove))
            }
        }
    }
}

@Composable
fun FileField(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun CaptionText(
    text: String
) {
    Text(
        text = text,
        color = MaterialTheme.additionalColors.lightText,
        style = MaterialTheme.typography.caption,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showSystemUi = true)
@Composable
private fun MediaStoreUiPreview() {
    AppTheme {
        val component = FakeMediaStoreComponent()
        MediaStoreContent(
            component.filter,
            component.mediaFiles!!,
            component::onCameraRequest,
            component::onChangeFilter,
            component::onFileClick,
            component::onFileRemoveClick,
            rememberLazyListState()
        )
    }
}

class FakeMediaStoreComponent : MediaStoreComponent {

    override var filter: TypeFilter = TypeFilter.All

    override val isCameraRequested: Boolean = false

    override var mediaFiles: List<MediaFileViewData>? = List(5) {
        MediaFileViewData.MOCK
    }

    override var selectedMediaFIle: DetailedImageFileViewData? = null

    override var isShowImageFileContent: Boolean = false

    override fun onLoadMedia() = Unit

    override fun onCameraRequest() = Unit

    override fun onResetCameraRequest() = Unit

    override fun onSaveBitmap(bitmap: Bitmap) = Unit

    override fun onChangeFilter(filter: TypeFilter) = Unit

    override fun onFileClick(uri: Uri) = Unit

    override fun onFileRemoveClick(uri: Uri) = Unit
}