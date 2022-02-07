package com.example.scoped_storage_example.core.ui.widgets

import android.app.Activity
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.scoped_storage_example.R

@Composable
fun Toolbar(
    title: String,
    modifier: Modifier = Modifier,
    backNavigationEnabled: Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = if (backNavigationEnabled) {
            { NavigationButton() }
        } else {
            null
        },
        modifier = modifier
    )
}

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    IconButton(onClick = {
        (context as Activity).onBackPressed()
    }, modifier = modifier) {
        Icon(
            painterResource(R.drawable.ic_back),
            contentDescription = null
        )
    }
}