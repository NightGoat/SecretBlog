package ru.nightgoat.secretblog.android.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.BlogTheme


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
                    .padding(16.dp),
            ) {
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.5f),
                    onClick = onLeftButtonClick,
                ) {
                    Text(leftButtonText)
                }
                SimpleSpacer(8)
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.5f),
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

@Preview
@Composable
fun AlertPreview() {
    BlogTheme {
        AppAlert(
            title = "Hello world",
            message = "Indeed hello world",
            leftButtonText = "No",
            rightButtonText = "Yes",
            onLeftButtonClick = { }) {

        }

    }
}