package com.example.scoped_storage_example.core.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

val MaterialTheme.additionalColors: AdditionalColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAdditionalColors.current