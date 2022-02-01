package com.example.scoped_storage_example.file_picker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
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
            FilePickerContent(
                modifier = Modifier.padding(it),
                onOpenFile = component::onOpenFile
            )
        }
    )
}

@Composable
private fun FilePickerContent(
    modifier: Modifier = Modifier,
    onOpenFile: (Uri) -> Unit,
) {
    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onOpenFile(uri)
        }
    }

    val multiplePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
//        uri?.let {
//            onOpenFile(uri)
//        }
    }

    ControlButton(
        text = "Open file",
        onClick = { pickerLauncher.launch("*/*") }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun FilePickerPreview() {
    AppTheme {
        FilePickerUi(FakeFilePickerComponent())
    }
}

class FakeFilePickerComponent : FilePickerComponent {

    override fun onOpenFile(uri: Uri) = Unit
}