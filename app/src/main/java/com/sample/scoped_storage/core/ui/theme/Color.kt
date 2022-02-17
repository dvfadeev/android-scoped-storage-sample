package com.sample.scoped_storage.core.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

object RawColors {
    val purpleA: Color = Color(0xFF4A148C)
    val purpleB: Color = Color(0xFF7C43BD)
    val whiteA: Color = Color(0xFFFFFFFF)
    val blackA: Color = Color(0xFF000000)
    val grayA: Color = Color(0xFFA1ACB4)
}

val ColorPalette = lightColors(
    primary = RawColors.purpleA,
    primaryVariant = RawColors.purpleB,
    secondary = RawColors.purpleA,
    secondaryVariant = RawColors.purpleB,
    background = RawColors.whiteA,
    surface = RawColors.whiteA,
    onPrimary = RawColors.whiteA,
    onSecondary = RawColors.whiteA,
    onBackground = RawColors.blackA,
    onSurface = RawColors.blackA
)

data class AdditionalColors(
    val lightText: Color = RawColors.grayA
)

val LocalAdditionalColors = staticCompositionLocalOf {
    AdditionalColors()
}