package ru.nightgoat.secretblog.android.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun BlogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }
    MaterialTheme(
        colors = colors,
        content = content
    )
}

private val DarkColors = darkColors(
    primary = AppColor.darkBlue,
    secondary = AppColor.blue,
    background = AppColor.darkBlue,
    onPrimary = AppColor.beige,
    onSecondary = AppColor.beige,
    onBackground = AppColor.beige,
    primaryVariant = AppColor.blue
)

private val LightColors = lightColors(
    primary = AppColor.blue,
    secondary = AppColor.greyBlue,
    background = AppColor.beige,
    primaryVariant = AppColor.blue
)