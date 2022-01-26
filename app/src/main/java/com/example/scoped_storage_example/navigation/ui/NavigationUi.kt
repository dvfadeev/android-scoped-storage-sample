package com.example.scoped_storage_example.navigation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.core.ui.theme.AppTheme

@Composable
fun NavigationUi(
    component: NavigationComponent,
    modifier: Modifier = Modifier
) {
    Text(
        text = "navigation"
    )
}

@Preview(showSystemUi = true)
@Composable
private fun NavigationUiPreview() {
    AppTheme {
        NavigationUi(FakeNavigationComponent())
    }
}

class FakeNavigationComponent : NavigationComponent {

    override fun onAppStorageClick() = Unit
}