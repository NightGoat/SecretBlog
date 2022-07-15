package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.nightgoat.kexcore.orZero
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.*
import ru.nightgoat.secretblog.android.presentation.composables.AppIcon
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.SecretBlogsState
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary

private const val TEXT_INPUT_MAX_LINES = 5
private val TOOLBAR_TEXT_SIZE = 20.sp
private val MESSAGE_TIME_STAMP_TEXT_SIZE = 12.sp
private val MESSAGE_TEXT_SIZE = 16.sp

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    effects: BlogEffect,
    dictionary: Dictionary
) {
    val listState = rememberLazyListState()
    when (effects) {
        is BlogEffect.ScrollToLastElement -> {
            LaunchedEffect(state) {
                listState.scrollToItem(state.visibleMessages.lastIndex.takeIf { it > 0 }.orZero())
            }
        }
    }
    ChatMainContent(
        state = state,
        listState = listState,
        dictionary = dictionary,
        onSendMessageClick = viewModel::addMessage,
        onShowHideButtonClick = {
            if (state.settings.isPinOnSecretVisibilitySet && state.secretBlogsState.isHidden()) {
                viewModel.dispatch(
                    GlobalAction.Navigate(
                        Screen.PinCode.route,
                        Screen.PinCode.IS_PINCODE_CHECK_ON_SECRET_VISIBILITY
                    )
                )
            } else {
                viewModel.dispatch(BlogAction.ReverseSecretBlogsVisibility)
            }
        },
        onMessageSelect = viewModel::onMessageSelected,
        onLongPress = {
            viewModel.dispatch(BlogAction.ReverseEditMode)
        },
        onDeleteMessagesClick = viewModel::deleteSelectedMessages,
        onSettingsClick = {
            viewModel.dispatch(GlobalAction.Navigate(Screen.Settings.route))
        }
    )
}

@Composable
private fun ChatMainContent(
    state: AppState = AppState(),
    listState: LazyListState = LazyListState(),
    dictionary: Dictionary = EnglishDictionary,
    onSendMessageClick: (String, Boolean) -> Unit = { _, _ -> },
    onShowHideButtonClick: () -> Unit = {},
    onMessageSelect: (BlogMessage, Boolean) -> Unit = { _, _ -> },
    onLongPress: () -> Unit = {},
    onDeleteMessagesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Toolbar(
            state = state,
            onShowHideButtonClick = onShowHideButtonClick,
            onCancelSelectionClick = onLongPress,
            onDeleteMessagesClick = onDeleteMessagesClick,
            onSettingsClick = onSettingsClick
        )
        Messages(
            modifier = Modifier.weight(1f),
            listState = listState,
            state = state,
            onMessageSelect = onMessageSelect,
            onLongPress = onLongPress
        )
        InputToolbar(
            dictionary = dictionary,
            onSendMessageClick = { message, isSecret ->
                onSendMessageClick(message, isSecret)
            }
        )
    }
}

@Composable
private fun Toolbar(
    state: AppState,
    onDeleteMessagesClick: () -> Unit,
    onShowHideButtonClick: () -> Unit,
    onCancelSelectionClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        elevation = defaultElevation,
        color = MaterialTheme.colors.primaryVariant,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.isEdit) {
                AppIcon(
                    drawableId = R.drawable.ic_outline_cancel_24,
                    contentDescription = "Cancel selection"
                ) {
                    onCancelSelectionClick()
                }
                SimpleSpacer(16)
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = TOOLBAR_TEXT_SIZE,
                    text = state.sizeOfSelectedMessages
                )
                AppIcon(
                    drawableId = R.drawable.ic_outline_delete_24,
                    contentDescription = "Delete messages"
                ) {
                    onDeleteMessagesClick()
                }
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = TOOLBAR_TEXT_SIZE,
                    text = stringResource(id = R.string.app_name)
                )
            }
            SimpleSpacer()
            val hideDrawable = when (state.secretBlogsState) {
                SecretBlogsState.VISIBLE -> R.drawable.ic_outline_visibility_off_24
                SecretBlogsState.HIDDEN -> R.drawable.ic_outline_visibility_24
            }
            AppIcon(
                drawableId = hideDrawable,
                contentDescription = "show / hide secret blogs"
            ) {
                onShowHideButtonClick()
            }
            SimpleSpacer()
            AppIcon(
                drawableId = R.drawable.ic_outline_settings_24,
                contentDescription = "settings"
            ) {
                onSettingsClick()
            }
        }
    }
}

@Composable
private fun Messages(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    state: AppState,
    onLongPress: () -> Unit,
    onMessageSelect: (BlogMessage, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.End
    ) {
        items(state.visibleMessages) { message ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (state.isEdit) {
                                    onMessageSelect(message, !message.isSelected)
                                }
                            },
                            onLongPress = {
                                onLongPress()
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = state.isEdit,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    Checkbox(
                        checked = message.isSelected,
                        onCheckedChange = { isChecked ->
                            onMessageSelect(message, isChecked)
                        }
                    )
                }
                if (!state.isEdit) {
                    SimpleSpacer()
                }
                MessageCard(
                    state = state,
                    message = message,
                    onLongPress = onLongPress,
                    onMessageSelect = { message, isSelected ->
                        onMessageSelect(message, isSelected)
                    }
                )
            }
        }
    }
}

@Composable
private fun MessageCard(
    state: AppState,
    message: BlogMessage,
    onMessageSelect: (BlogMessage, Boolean) -> Unit,
    onLongPress: () -> Unit
) {
    var backgroundColor = AppColor.elephantBone
    var textColor = Color.Black
    var timeStampColor = Color.DarkGray
    if (message.isSecret) {
        backgroundColor = AppColor.blue
        timeStampColor = Color.Black
        textColor = Color.White
    }
    Card(
        modifier = Modifier
            .padding(defaultPadding),
        backgroundColor = backgroundColor,
    ) {
        Column(
            modifier = Modifier.padding(defaultPadding),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message.timeFormatted(),
                fontSize = MESSAGE_TIME_STAMP_TEXT_SIZE,
                color = timeStampColor
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = message.text,
                fontSize = MESSAGE_TEXT_SIZE,
                color = textColor
            )
        }
    }
}

@Composable
private fun InputToolbar(
    dictionary: Dictionary,
    onSendMessageClick: (String, Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    BlogTheme {
        ChatMainContent(
            state = AppState(
                blogMessages = listOf(
                    BlogMessage()
                ),
                secretBlogsState = SecretBlogsState.VISIBLE,
                isEdit = false
            )
        )
    }
}