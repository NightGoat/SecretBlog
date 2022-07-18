package ru.nightgoat.secretblog.android.presentation.screens.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.*
import ru.nightgoat.secretblog.android.presentation.composables.AppIcon
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.ChatMessagesEditMode
import ru.nightgoat.secretblog.models.orEmpty
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary


private const val TEXT_INPUT_MAX_LINES = 5

@Composable
fun InputToolbar(
    prerenderedText: BlogMessage? = null,
    state: AppState = AppState(),
    dictionary: Dictionary,
    onSendMessageClick: (String, Boolean) -> Unit,
    onFinishEditMessage: (BlogMessage) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    prerenderedText?.let {
        text = it.text
    }
    Surface(
        elevation = defaultElevation,
        color = MaterialTheme.colors.primaryVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = defaultPadding),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send
                ),
                shape = RoundedCornerShape(CornerSize(defaultCornerRadius)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    textColor = AppColor.elephantBone,
                    cursorColor = AppColor.elephantBone,
                    placeholderColor = AppColor.elephantBone
                ),
                placeholder = {
                    Text(dictionary.messageTextPlaceHolder)
                },
                value = text,
                onValueChange = {
                    text = it
                },
                maxLines = TEXT_INPUT_MAX_LINES
            )
            if (state.editMode !is ChatMessagesEditMode.Edit) {
                AppIcon(
                    drawableId = R.drawable.ic_outline_cancel_schedule_send_24,
                    contentDescription = "Send silently"
                ) {
                    onSendMessageClick(text, true)
                    text = ""
                }
                SimpleSpacer()
                AppIcon(
                    drawableId = R.drawable.ic_outline_send_24,
                    contentDescription = "Send"
                ) {
                    onSendMessageClick(text, false)
                    text = ""
                }
            } else {
                AppIcon(
                    drawableId = R.drawable.ic_outline_edit_24,
                    contentDescription = dictionary.edit
                ) {
                    onFinishEditMessage(prerenderedText?.copy(text = text).orEmpty())
                    text = ""
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InputToolbarPreview() {
    BlogTheme {
        InputToolbar(dictionary = EnglishDictionary, onSendMessageClick = { _, _ -> })
    }
}