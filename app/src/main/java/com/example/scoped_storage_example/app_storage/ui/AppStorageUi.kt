package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.core.ui.theme.AppTheme

@Composable
fun AppStorageUi(
    component: AppStorageComponent,
    modifier: Modifier = Modifier
) {
    Text(
        text = "AppStorage"
    )
}

@Preview(showSystemUi = true)
@Composable
private fun AppStorageUiPreview() {
    AppTheme {
        AppStorageUi(FakeAppStorageComponent())
    }
}

class FakeAppStorageComponent : AppStorageComponent
