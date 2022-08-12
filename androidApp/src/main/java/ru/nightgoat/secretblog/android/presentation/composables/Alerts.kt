package ru.nightgoat.secretblog.android.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.data.ButtonData

private val alertButtonHeight = 48.dp
private val buttonsPadding = 16.dp
private const val buttonSpacing = 8

@Composable
fun AppAlert(
    title: String,
    message: String,
    leftButtonData: ButtonData,
    rightButtonData: ButtonData,
) {
    AlertDialog(
        onDismissRequest = leftButtonData.onClick,
        title = {
            Text(title)
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(buttonsPadding),
            ) {
                Button(
                    modifier = Modifier
                        .height(alertButtonHeight)
                        .weight(0.5f),
                    onClick = leftButtonData.onClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = leftButtonData.color)
                ) {
                    AlertButtonText(leftButtonData)
                }
                SimpleSpacer(buttonSpacing)
                Button(
                    modifier = Modifier
                        .height(alertButtonHeight)
                        .weight(0.5f),
                    onClick = rightButtonData.onClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = rightButtonData.color)
                ) {
                    AlertButtonText(rightButtonData)
                }
            }
        },
        text = {
            Text(text = message)
        }
    )
}

@Composable
private fun AlertButtonText(buttonData: ButtonData) {
    Text(
        text = buttonData.text,
        color = Color.White
    )
}

@Preview
@Composable
fun AlertPreview() {
    BlogTheme {
        AppAlert(
            title = "Hello world",
            message = "Indeed hello world",
            leftButtonData = ButtonData(
                text = "No"
            ),
            rightButtonData = ButtonData(
                text = "Yes", color = AppColor.red
            )
        )
    }
}