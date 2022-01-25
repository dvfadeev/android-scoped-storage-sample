package com.example.scoped_storage_example.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scoped_storage_example.core.ui.theme.AppTheme

@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    AppTheme {
        val backgroundColor = MaterialTheme.colors.background

        Surface(
            color = backgroundColor,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Init"
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}

class FakeRootComponent : RootComponent {

}