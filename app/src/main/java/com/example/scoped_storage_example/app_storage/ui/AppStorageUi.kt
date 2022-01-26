package com.example.scoped_storage_example.app_storage.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scoped_storage_example.R
import com.example.scoped_storage_example.core.ui.theme.AppTheme
import com.example.scoped_storage_example.core.ui.widgets.ControlButton
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
                modifier = Modifier.padding(it),
                onAddLogClick = component::onAddLogClick,
                onSaveLogClick = component::onSaveLogClick
            )
        }
    )
}

@Composable
private fun AppStorageContent(
    modifier: Modifier = Modifier,
    onAddLogClick: () -> Unit,
    onSaveLogClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        AppStorageCardItem(
            text = stringResource(id = R.string.app_storage_add_log_text),
            buttonText = stringResource(id = R.string.app_storage_add_log_button),
            onClick = onAddLogClick
        )

        AppStorageCardItem(
            text = stringResource(id = R.string.app_storage_save_log_text),
            buttonText = stringResource(id = R.string.app_storage_save_log_button),
            onClick = onSaveLogClick
        )
    }
}

@Composable
private fun AppStorageCardItem(
    text: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 7.dp,
        modifier = modifier.fillMaxWidth()
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


@Preview(showSystemUi = true)
@Composable
private fun AppStorageUiPreview() {
    AppTheme {
        AppStorageUi(FakeAppStorageComponent())
    }
}

class FakeAppStorageComponent : AppStorageComponent {

    override fun onAddLogClick() = Unit

    override fun onSaveLogClick() = Unit
}
