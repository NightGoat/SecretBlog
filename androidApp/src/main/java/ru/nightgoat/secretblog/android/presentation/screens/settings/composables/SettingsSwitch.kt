package ru.nightgoat.secretblog.android.presentation.screens.settings.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.core.AppState

@Composable
fun SettingsSwitch(
    text: String,
    state: AppState,
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
            modifier = Modifier.padding(start = defaultPadding),
            text = text
        )
        Switch(checked = isChecked, onCheckedChange = onClick)
    }
}