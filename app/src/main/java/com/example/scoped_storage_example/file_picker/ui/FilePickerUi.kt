package com.example.scoped_storage_example.file_picker.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.Toolbar

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

        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun FilePickerPreview() {
    AppTheme {
        FilePickerUi(FakeFilePickerComponent())
    }
}

class FakeFilePickerComponent : FilePickerComponent