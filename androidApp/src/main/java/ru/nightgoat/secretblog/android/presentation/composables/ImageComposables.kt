package ru.nightgoat.secretblog.android.presentation.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import ru.nightgoat.secretblog.android.presentation.appIconSize
import ru.nightgoat.secretblog.android.presentation.defaultPadding

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int,
    contentDescription: String = "",
    color: Color = MaterialTheme.colors.onPrimary,
    onClick: () -> Unit
) {
    Image(
        modifier = Modifier
            .size(appIconSize)
            .clickable(onClick = onClick)
            .padding(defaultPadding)
            .then(modifier),
        painter = painterResource(id = drawableId),
        colorFilter = ColorFilter.tint(color),
        contentDescription = contentDescription
    )
}