package ru.nightgoat.secretblog.android.presentation.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.AppIcon
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultCornerRadius
import ru.nightgoat.secretblog.android.presentation.defaultElevation
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.defaultSmallPadding
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.ChatMessagesEditMode
import ru.nightgoat.secretblog.models.orEmpty
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.MultiLanguageDictionary
import ru.nightgoat.secretblog.utils.GlobalConstants.TWITTER_MESSAGE_MAX_LENGTH


private const val TEXT_INPUT_MAX_LINES = 5
private const val HELPER_MESSAGE_FONT_SIZE = 12

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
                .padding(horizontal = defaultPadding)
                .padding(bottom = defaultSmallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(end = defaultPadding)) {
                TextField(
                    modifier = Modifier,
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
                if (state.settings.isSendToTwitterFeatureOn) {
                    val messageCounter = TWITTER_MESSAGE_MAX_LENGTH - text.length
                    val color = if (messageCounter > 0) {
                        Color.Unspecified
                    } else AppColor.beige
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = defaultPadding),
                        text = "$messageCounter",
                        textAlign = TextAlign.End,
                        fontSize = HELPER_MESSAGE_FONT_SIZE.sp,
                        color = color
                    )
                }
            }
            if (state.editMode !is ChatMessagesEditMode.Edit) {
                AppIcon(
                    drawableId = R.drawable.incognito_svgrepo_com,
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
        InputToolbar(dictionary = MultiLanguageDictionary(), onSendMessageClick = { _, _ -> })
    }
}