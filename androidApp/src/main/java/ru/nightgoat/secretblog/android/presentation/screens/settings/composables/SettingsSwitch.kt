package ru.nightgoat.secretblog.android.presentation.screens.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.defaultPadding

@Composable
fun SettingsSwitch(
    text: String,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(!isChecked)
            }
            .padding(top = 4.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = defaultPadding),
            overflow = TextOverflow.Visible,
            text = text
        )

        Switch(
            modifier = Modifier.weight(0.1f),
            checked = isChecked,
            onCheckedChange = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsSwitchPreview() {
    BlogTheme {
        var isChecked by remember { mutableStateOf(true) }
        SettingsSwitch(
            text = "Really Long Text, i mean it! It is really really really long",
            isChecked = isChecked,
            onClick = { isChecked = !isChecked }
        )
    }
}