package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
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
            AppStorageContent(
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
private fun AppStorageContent(
    modifier: Modifier = Modifier
) {

}

@Preview(showSystemUi = true)
@Composable
private fun AppStorageUiPreview() {
    AppTheme {
        AppStorageUi(FakeAppStorageComponent())
    }
}

class FakeAppStorageComponent : AppStorageComponent
