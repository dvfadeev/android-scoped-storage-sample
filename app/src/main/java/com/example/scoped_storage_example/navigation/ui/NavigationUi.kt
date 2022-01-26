package com.example.scoped_storage_example.navigation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme

@Composable
fun NavigationUi(
    component: NavigationComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        content = {
            NavigationContent(
                onAppStorageClick = component::onAppStorageClick,
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
private fun NavigationContent(
    onAppStorageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(top = 20.dp)
    ) {
        NavigationButton(
            text = stringResource(id = R.string.app_storage_title),
            onClick = onAppStorageClick
        )
    }
}

@Composable
private fun NavigationButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
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