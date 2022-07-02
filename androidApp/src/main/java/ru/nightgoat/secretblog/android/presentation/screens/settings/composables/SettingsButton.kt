package ru.nightgoat.secretblog.android.presentation.screens.settings.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.defaultPadding

@Composable
fun SettingsButton(
    text: String,
    imageId: Int,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = defaultPadding)
            .padding(top = 14.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(start = defaultPadding),
                text = text
            )
            Image(
                modifier = Modifier.padding(end = 12.dp),
                painter = painterResource(id = imageId),
                contentDescription = contentDescription,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }
    }
}