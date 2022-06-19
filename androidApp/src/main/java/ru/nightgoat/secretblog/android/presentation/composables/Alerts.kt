package ru.nightgoat.secretblog.android.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.nightgoat.secretblog.android.presentation.defaultPadding


@Composable
fun AppAlert(
    title: String,
    message: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onLeftButtonClick,
        title = {
            Text(title)
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding),
            ) {
                Button(
                    modifier = Modifier.weight(0.5f),
                    onClick = onLeftButtonClick,
                ) {
                    Text(leftButtonText)
                }
                SimpleSpacer()
                Button(
                    modifier = Modifier.weight(0.5f),
                    onClick = onRightButtonClick
                ) {
                    Text(rightButtonText)
                }
            }
        },
        text = {
            Text(text = message)
        }
    )
}