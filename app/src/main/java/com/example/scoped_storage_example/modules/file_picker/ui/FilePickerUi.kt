package com.example.scoped_storage_example.modules.file_picker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.scoped_storage_example.core.utils.AvailableFilters
import com.example.scoped_storage_example.core.utils.TypeFilter
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
                filter = component.filter,
                documentFiles = component.documentFiles,
                onChangeFilter = component::onChangeFilter,
                onOpenFileClick = component::onOpenFileClick,
                onOpenFilesClick = component::onOpenFilesClick,
                onRemoveFileClick = component::onRemoveFileClick
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FilePickerContent(
    modifier: Modifier = Modifier,
    filter: TypeFilter,
    documentFiles: List<DocumentFileViewData>,
    onChangeFilter: (TypeFilter) -> Unit,
    onOpenFileClick: (Uri) -> Unit,
    onOpenFilesClick: (List<Uri>) -> Unit,
    onRemoveFileClick: (Uri) -> Unit
) {
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            onOpenFileClick(uri)
        }
    }
    val multiplePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        onOpenFilesClick(uris)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        FilterSelector(
            filter = filter,
            onChangeFilter = onChangeFilter,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

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
                    onClick = { pickerLauncher.launch(arrayOf(filter.mime)) }
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
                    onClick = { multiplePickerLauncher.launch(arrayOf(filter.mime)) }
                )
            }
        }

        if (documentFiles.isNotEmpty()) {
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
                    .padding(horizontal = 16.dp)
            )

            HorizontalPager(
                state = pagerState
            ) {
                if (size > it) {
                    val file = documentFiles[it]
                    DocumentFileItem(
                        data = file,
                        onClick = { onRemoveFileClick(file.uri) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterSelector(
    filter: TypeFilter,
    onChangeFilter: (TypeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = stringResource(id = filter.resource),
            onValueChange = { },
            label = { Text(text = stringResource(id = R.string.filter_title)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            AvailableFilters.list.forEach { filter ->
                DropdownMenuItem(
                    onClick = {
                        onChangeFilter(filter)
                        expanded = false
                    }
                ) {
                    Text(text = stringResource(id = filter.resource))
                }
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
            onClick = onPreviousClick,
            modifier = Modifier.weight(1f)
        )

        SelectableButton(
            text = stringResource(id = R.string.file_picker_next),
            isSelected = false,
            isEnabled = isNextEnabled,
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
            onClick = onNextClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DocumentFileItem(
    data: DocumentFileViewData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCard(modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_name), text = data.name)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_id), text = data.id)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_flags), text = data.flags)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_mime_type), text = data.mimeType)
            DocumentFileField(title = stringResource(id = R.string.file_picker_file_size), text = data.sizeKb)

            data.dateModified?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_date_modified), text = it)
            }
            data.icon?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_icon), text = it)
            }
            data.summary?.let {
                DocumentFileField(title = stringResource(id = R.string.file_picker_file_summary), text = it)
            }

            ControlButton(
                text = stringResource(id = R.string.file_picker_file_remove),
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            )

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

    override var filter: TypeFilter = TypeFilter.All

    override var documentFiles: List<DocumentFileViewData> = listOf()

    override fun onChangeFilter(filter: TypeFilter) = Unit

    override fun onOpenFileClick(uri: Uri) = Unit

    override fun onOpenFilesClick(uris: List<Uri>) = Unit

    override fun onRemoveFileClick(uri: Uri) = Unit
}