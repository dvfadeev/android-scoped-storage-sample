package com.sample.scoped_storage.modules.media_store.ui

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.ui.theme.AppTheme
import com.sample.scoped_storage.core.ui.theme.additionalColors
import com.sample.scoped_storage.core.ui.widgets.*
import com.sample.scoped_storage.core.utils.AvailableFilters
import com.sample.scoped_storage.core.utils.TypeFilter

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
            MediaStoreContent(
                component = component,
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
private fun MediaStoreContent(
    component: MediaStoreComponent,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val scrollState = rememberLazyListState()
        SlideAnimationScreen(
            firstScreen = {
                MediaStoreListContent(
                    isRefreshing = component.isRefreshing,
                    filter = component.filter,
                    mediaFiles = component.mediaFiles,
                    onChangeMediaType = component::onChangeFilter,
                    onLoadMedia = component::onLoadMedia,
                    onSaveBitmap = component::onSaveBitmap,
                    onFileClick = component::onFileClick,
                    onFileLongClick = component::onFileLongClick,
                    onFileRemoveClick = component::onFileRemoveClick,
                    scrollState = scrollState,
                    modifier = modifier
                )
            },
            secondScreen = {
                component.selectedMediaFile?.let {
                    FileContent(file = it)
                } ?: run {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Center))
                    }
                }
            },
            isShowSecondScreen = component.isShowImageFileContent
        )
        component.dialogData?.let {
            Dialog(data = it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaStoreListContent(
    isRefreshing: Boolean,
    filter: TypeFilter,
    mediaFiles: List<MediaFileViewData>?,
    onChangeMediaType: (TypeFilter) -> Unit,
    onLoadMedia: () -> Unit,
    onSaveBitmap: (Bitmap) -> Unit,
    onFileClick: (Uri) -> Unit,
    onFileLongClick: (Uri) -> Unit,
    onFileRemoveClick: (Uri) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo ->
        photo?.let {
            onSaveBitmap(it)
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onLoadMedia,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .verticalScrollbar(scrollState, 4.dp, color = MaterialTheme.colors.primary),
            state = scrollState
        ) {
            item {
                ControlButton(
                    text = stringResource(id = R.string.media_store_take_photo),
                    onClick = { cameraLauncher.launch() }
                )
            }

            stickyHeader {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    Box {
                        MediaTypeSelector(
                            filter = filter,
                            onChangeFilter = {
                                onChangeMediaType(it)
                            },
                            modifier = Modifier.align(Center)
                        )
                    }
                }
            }

            mediaFiles?.let { files ->
                items(files) {
                    MediaFileItem(
                        data = it,
                        onFileClick = onFileClick,
                        onFileLongClick = onFileLongClick,
                        onFileRemoveClick = { uri ->
                            onFileRemoveClick(uri)
                        }
                    )
                }
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
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 7.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp)
        ) {
            Column(
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
                FileField(title = stringResource(id = R.string.media_store_file_resolution), text = file.resolution)
                FileField(title = stringResource(id = R.string.media_store_file_duration), text = file.duration)
            }
        }

        Text(
            text = stringResource(id = R.string.media_store_file_content_preview),
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        val conf = LocalConfiguration.current

        ImageViewer(
            uri = file.uri,
            size = conf.screenWidthDp.dp,
            type = file.mimeType,
            isCropEnabled = false,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
    onFileLongClick: (Uri) -> Unit,
    onFileRemoveClick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val fileOpenFailedToast = Toast.makeText(
        LocalContext.current,
        stringResource(id = R.string.media_store_file_open_fail),
        Toast.LENGTH_SHORT
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        data.uri?.let {
                            onFileClick(it)
                        } ?: fileOpenFailedToast.show()
                    },
                    onLongClick = {
                        data.uri?.let {
                            expanded = true
                            onFileLongClick(it)
                        } ?: fileOpenFailedToast.show()
                    }
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

        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
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
}

@Composable
fun FileField(
    title: String,
    text: String?,
    modifier: Modifier = Modifier
) {
    if (text == null) return
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption
        )
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.weight(1f)
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
        MediaStoreContent(
            FakeMediaStoreComponent()
        )
    }
}

class FakeMediaStoreComponent : MediaStoreComponent {

    override val dialogData: DialogData? = null

    override val isRefreshing: Boolean = false

    override var filter: TypeFilter = TypeFilter.All

    override var mediaFiles: List<MediaFileViewData>? = List(5) { MediaFileViewData.MOCK }

    override val selectedUri: Uri? = null

    override var selectedMediaFile: DetailedImageFileViewData? = null

    override var isShowImageFileContent: Boolean = false

    override fun onLoadMedia() = Unit

    override fun onSaveBitmap(bitmap: Bitmap) = Unit

    override fun onChangeFilter(filter: TypeFilter) = Unit

    override fun onFileClick(uri: Uri) = Unit

    override fun onFileLongClick(uri: Uri) = Unit

    override fun onFileRemoveClick(uri: Uri) = Unit
}