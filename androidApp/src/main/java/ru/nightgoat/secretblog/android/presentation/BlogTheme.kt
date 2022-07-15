package ru.nightgoat.secretblog.android.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ru.nightgoat.secretblog.models.ThemeType

@Composable
fun BlogTheme(
    selectedTheme: ThemeType = ThemeType.System,
    content: @Composable () -> Unit
) {
    val colors = when (selectedTheme) {
        ThemeType.System -> {
            if (isSystemInDarkTheme()) {
                DarkColors
            } else {
                LightColors
            }
        }
        ThemeType.Dark -> DarkColors
        ThemeType.Light -> LightColors
    }
    MaterialTheme(
        colors = colors,
        content = content
    )
}

private val DarkColors = darkColors(
    primary = AppColor.blue,
    secondary = AppColor.blue,
    background = AppColor.darkBlue,
    onPrimary = AppColor.beige,
    onSecondary = AppColor.beige,
    onBackground = AppColor.beige,
    primaryVariant = AppColor.darkBlue
)

private val LightColors = lightColors(
    primary = AppColor.darkBlue,
    secondary = AppColor.greyBlue,
    background = AppColor.beige,
    primaryVariant = AppColor.blue
)