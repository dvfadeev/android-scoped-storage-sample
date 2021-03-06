package com.sample.scoped_storage.navigation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.scoped_storage.R
import com.sample.scoped_storage.core.ui.theme.AppTheme
import com.sample.scoped_storage.core.ui.widgets.Toolbar

@Composable
fun NavigationUi(
    component: NavigationComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Toolbar(title = stringResource(id = R.string.app_name))
        },
        content = {
            NavigationContent(
                onAppStorageClick = component::onAppStorageClick,
                onMediaStoreClick = component::onMediaStoreClick,
                onFilePickerClick = component::onFilePickerClick,
                modifier = Modifier.padding(it)
            )
        }
    )
}

@Composable
private fun NavigationContent(
    onAppStorageClick: () -> Unit,
    onMediaStoreClick: () -> Unit,
    onFilePickerClick: () -> Unit,
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

        NavigationButton(
            text = stringResource(id = R.string.media_store_title),
            onClick = onMediaStoreClick
        )

        NavigationButton(
            text = stringResource(id = R.string.file_picker_title),
            onClick = onFilePickerClick
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

    override fun onMediaStoreClick() = Unit

    override fun onFilePickerClick() = Unit
}