package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
import com.example.scoped_storage_example.core.ui.widgets.ImageViewer
import com.example.scoped_storage_example.core.ui.widgets.SelectableButton
import com.example.scoped_storage_example.core.ui.widgets.Toolbar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun FilePickerUi(
    component: FilePickerComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.file_picker_title),
                backNavigationEnabled = true
            )
        },
        content = {
            FilePickerContent(
                modifier = Modifier.padding(it),
                documentFiles = component.documentFiles,
                onOpenFile = component::onOpenFile,
                onOpenFiles = component::onOpenFiles
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FilePickerContent(
    modifier: Modifier = Modifier,
    documentFiles: List<DocumentFileViewData>?,
    onOpenFile: (Uri) -> Unit,
    onOpenFiles: (List<Uri>) -> Unit
) {
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onOpenFile(uri)
        }
    }

    val multiplePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        onOpenFiles(uris)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        CustomCard(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.file_picker_launch_text),
                    modifier = Modifier.weight(1f)
                )
                ControlButton(
                    text = stringResource(id = R.string.file_picker_launch),
                    onClick = { pickerLauncher.launch("*/*") }
                )
            }
        }

        CustomCard(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.file_picker_launch_multiply_text),
                    modifier = Modifier.weight(1f)
                )
                ControlButton(
                    text = stringResource(id = R.string.file_picker_launch),
                    onClick = { multiplePickerLauncher.launch("*/*") }
                )
            }
        }

        if (documentFiles != null) {
            val scope = rememberCoroutineScope()
            val size = documentFiles.size
            val pagerState = rememberPagerState(pageCount = documentFiles.size, initialOffscreenLimit = 2)
            val page = pagerState.currentPage

            PageSwitcher(
                isPreviousEnabled = size > 1 && page > 0,
                isNextEnabled = size > 1 && page < size - 1,
                onPreviousClick = {
                    scope.launch {
                        pagerState.scrollToPage(page - 1)
                    }
                },
                onNextClick = {
                    scope.launch {
                        pagerState.scrollToPage(page + 1)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            HorizontalPager(
                state = pagerState
            ) {
                DocumentFileItem(data = documentFiles[it])
            }
        }
    }
}

@Composable
private fun PageSwitcher(
    modifier: Modifier = Modifier,
    isPreviousEnabled: Boolean,
    isNextEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        SelectableButton(
            text = stringResource(id = R.string.file_picker_previous),
            isSelected = false,
            isEnabled = isPreviousEnabled,
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
            onClick = onPreviousClick
        )

        SelectableButton(
            text = stringResource(id = R.string.file_picker_next),
            isSelected = false,
            isEnabled = isNextEnabled,
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
            onClick = onNextClick
        )
    }
}

@Composable
private fun DocumentFileItem(
    data: DocumentFileViewData,
    modifier: Modifier = Modifier,
) {
    CustomCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_name), text = data.name)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_id), text = data.id)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_flags), text = data.flags)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_mime_type), text = data.mimeType)

            data.dateModified?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_date_modified), text = it)
            }
            data.icon?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_icon), text = it)
            }
            data.summary?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_summary), text = it)
            }

            val conf = LocalConfiguration.current
            val size = conf.screenWidthDp.dp - 96.dp

            ImageViewer(
                uri = data.uri,
                size = size,
                type = data.mimeType,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun CustomCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit
) {
    Card(
        backgroundColor = backgroundColor,
        elevation = 7.dp,
        modifier = modifier.fillMaxWidth(),
        content = content
    )
}

@Composable
private fun DocumentFileField(
    title: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FilePickerPreview() {
    AppTheme {
        FilePickerUi(FakeFilePickerComponent())
    }
}

class FakeFilePickerComponent : FilePickerComponent {

    override var documentFiles: List<DocumentFileViewData>? = null

    override fun onOpenFile(uri: Uri) = Unit

    override fun onOpenFiles(uris: List<Uri>) = Unit
}