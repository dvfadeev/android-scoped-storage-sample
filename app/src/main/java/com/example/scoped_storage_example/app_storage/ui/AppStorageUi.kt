package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
import com.example.scoped_storage_example.core.ui.widgets.SelectableButton
import com.example.scoped_storage_example.core.ui.widgets.SlideAnimationScreen
import com.example.scoped_storage_example.core.ui.widgets.Toolbar

@Composable
fun AppStorageUi(
    component: AppStorageComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(
                title = stringResource(id = R.string.app_storage_title),
                backNavigationEnabled = true
            )
        },
        content = {
            SlideAnimationScreen(
                firstScreen = {
                    AppStorageContent(
                        modifier = Modifier.padding(it),
                        isInternalStorage = component.isInternalStorage,
                        files = component.files,
                        onToggleStorageClick = component::onToggleStorageClick,
                        onSaveLogClick = component::onSaveLogClick,
                        onFileOpenClick = component::onFileOpenClick,
                        onFileRemoveClick = component::onFileRemoveClick
                    )
                },
                secondScreen = {
                    FileContent(
                        modifier = Modifier.padding(it),
                        file = component.selectedFile
                    )
                },
                isShowSecondScreen = component.isShowFileContent
            )
        }
    )
}

@Composable
private fun AppStorageContent(
    modifier: Modifier = Modifier,
    isInternalStorage: Boolean,
    files: List<FileViewData>,
    onToggleStorageClick: () -> Unit,
    onSaveLogClick: () -> Unit,
    onFileOpenClick: (String) -> Unit,
    onFileRemoveClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        AppStorageToggleItem(
            isInternalStorage = isInternalStorage,
            onClick = onToggleStorageClick,
        )

        AppStorageCardItem(
            text = stringResource(id = R.string.app_storage_save_log_text),
            buttonText = stringResource(id = R.string.app_storage_save_log_button),
            onClick = onSaveLogClick
        )

        DirectoriesItem(
            files = files,
            onFileOpenClick = onFileOpenClick,
            onFileRemoveClick = onFileRemoveClick
        )
    }
}

@Composable
private fun FileContent(
    modifier: Modifier = Modifier,
    file: FileContentViewData?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        if (file == null) {
            CircularProgressIndicator()
        } else {
            Column {
                Text(
                    text = stringResource(id = R.string.app_storage_file_name),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.h6
                )
                Divider(
                    color = MaterialTheme.colors.onBackground,
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .alpha(0.2f)
                )
            }

            Text(
                text = file.content,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
private fun AppStorageToggleItem(
    isInternalStorage: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_storage_type_text),
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                SelectableButton(
                    text = stringResource(id = R.string.app_storage_type_internal),
                    isSelected = isInternalStorage,
                    onClick = onClick,
                    shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                )

                SelectableButton(
                    text = stringResource(id = R.string.app_storage_type_external),
                    isSelected = !isInternalStorage,
                    onClick = onClick,
                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                )
            }
        }
    }
}


@Composable
private fun AppStorageCardItem(
    text: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCard(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                text = buttonText,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun DirectoriesItem(
    files: List<FileViewData>,
    onFileOpenClick: (String) -> Unit,
    onFileRemoveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCard(
        modifier = modifier.padding(bottom = 32.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onPrimary,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            items(files) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    FileItem(
                        file = it,
                        onClick = { onFileOpenClick(it.name) },
                        onLongClick = { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                onFileRemoveClick(it.name)
                                expanded = false
                            }
                        ) {
                            Text(stringResource(id = R.string.app_storage_file_remove))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileItem(
    file: FileViewData,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Text(
            text = file.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        )
        Divider(
            color = MaterialTheme.colors.onPrimary,
            thickness = 2.dp,
            modifier = Modifier
                .padding(end = 8.dp)
                .alpha(0.4f)
        )
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

@Preview(showSystemUi = true)
@Composable
private fun AppStorageUiPreview() {
    AppTheme {
        AppStorageUi(FakeAppStorageComponent())
    }
}

class FakeAppStorageComponent : AppStorageComponent {

    override var isInternalStorage: Boolean = true

    override var files: List<FileViewData> = FileViewData.mocks()

    override var selectedFile: FileContentViewData? = null

    override var isShowFileContent: Boolean = false

    override fun onToggleStorageClick() = Unit

    override fun onSaveLogClick() = Unit

    override fun onFileOpenClick(fileName: String) = Unit

    override fun onFileRemoveClick(fileName: String) = Unit
}
