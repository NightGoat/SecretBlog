package ru.nightgoat.secretblog.android.presentation.screens.settings.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.defaultPadding

@Composable
fun SettingsDropdown(
    text: String,
    selections: List<String>,
    initialSelection: String,
    onClick: (Int, String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initialSelection) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(top = 12.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = defaultPadding),
            text = text
        )
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = selected)
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                contentDescription = "",
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onBackground)
            )
            DropdownMenu(expanded = isExpanded, onDismissRequest = {
                isExpanded = !isExpanded
            }) {
                selections.forEachIndexed { index, selection ->
                    DropdownMenuItem(onClick = {
                        selected = selection
                        onClick(index, selection)
                        isExpanded = !isExpanded
                    }) {
                        Text(text = selection)
                    }
                }
            }
        }
    }
}