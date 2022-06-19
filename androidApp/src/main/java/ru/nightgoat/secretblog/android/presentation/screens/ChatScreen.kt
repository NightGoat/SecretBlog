package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.SecretBlogsState
import ru.nightgoat.secretblog.providers.strings.Dictionary

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
                listState.scrollToItem(state.visibleMessages.lastIndex.takeIf { it > 0 }
                    ?: 0)
            }
        }
    }
    MainContent(
        state = state,
        listState = listState,
        onSendMessageClick = viewModel::addMessage,
        onClearButtonClick = viewModel::clearDB,
        onShowHideButtonClick = viewModel::reverseVisibility,
        onMessageSelect = viewModel::onMessageSelected,
        onLongPress = viewModel::reverseEditMode,
        onDeleteMessagesClick = viewModel::deleteSelectedMessages,
        onSettingsClick = {
            viewModel.dispatch(GlobalAction.Navigate(Screen.Settings.route))
        }
    )
}

@Composable
private fun MainContent(
    state: AppState = AppState(),
    listState: LazyListState = LazyListState(),
    onSendMessageClick: (String, Boolean) -> Unit = { _, _ -> },
    onClearButtonClick: () -> Unit = {},
    onShowHideButtonClick: () -> Unit = {},
    onMessageSelect: (BlogMessage, Boolean) -> Unit = { _, _ -> },
    onLongPress: () -> Unit = {},
    onDeleteMessagesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Surface(color = Color.LightGray) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Toolbar(
                state = state,
                onClearAllButtonClick = onClearButtonClick,
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
            InputToolbar { message, isSecret ->
                onSendMessageClick(message, isSecret)
            }
        }
    }
}

@Composable
private fun Toolbar(
    state: AppState,
    onClearAllButtonClick: () -> Unit,
    onDeleteMessagesClick: () -> Unit,
    onShowHideButtonClick: () -> Unit,
    onCancelSelectionClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        elevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.isEdit) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onCancelSelectionClick()
                        }
                        .padding(defaultPadding),
                    painter = painterResource(id = R.drawable.ic_outline_cancel_24),
                    contentDescription = "Cancel selection"
                )
                SimpleSpacer(16)
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    text = state.sizeOfSelectedMessages
                )
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    text = stringResource(id = R.string.app_name)
                )
            }
            Image(
                modifier = Modifier
                    .clickable {
                        if (state.isEdit) {
                            onDeleteMessagesClick()
                        } else {
                            onClearAllButtonClick()
                        }
                    }
                    .padding(defaultPadding),
                painter = painterResource(id = R.drawable.ic_outline_delete_24),
                contentDescription = "Delete all"
            )
            SimpleSpacer()
            val hideDrawable = when (state.secretBlogsState) {
                SecretBlogsState.VISIBLE -> R.drawable.ic_outline_visibility_off_24
                SecretBlogsState.HIDDEN -> R.drawable.ic_outline_visibility_24
            }
            Image(
                modifier = Modifier
                    .clickable {
                        onShowHideButtonClick()
                    }
                    .padding(defaultPadding),
                painter = painterResource(id = hideDrawable),
                contentDescription = "show / hide secret blogs"
            )
            SimpleSpacer()
            Image(
                modifier = Modifier
                    .clickable {
                        onSettingsClick()
                    }
                    .padding(defaultPadding),
                painter = painterResource(id = R.drawable.ic_outline_settings_24),
                contentDescription = "settings"
            )
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
    var backgroundColor = Color.White
    var textColor = Color.Black
    var timeStampColor = Color.DarkGray
    if (message.isSecret) {
        backgroundColor = MaterialTheme.colors.primary
        textColor = Color.White
        timeStampColor = Color.White
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
                fontSize = 12.sp,
                color = timeStampColor
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = message.text,
                fontSize = 16.sp,
                color = textColor
            )
        }
    }
}

@Composable
private fun InputToolbar(
    onSendMessageClick: (String, Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(defaultPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = defaultPadding),
            value = text,
            onValueChange = {
                text = it
            })
        Image(
            modifier = Modifier
                .clickable {
                    onSendMessageClick(text, true)
                    text = ""
                }
                .padding(defaultPadding),
            painter = painterResource(id = R.drawable.ic_outline_cancel_schedule_send_24),
            contentDescription = "Send silently"
        )
        SimpleSpacer()
        Image(
            modifier = Modifier
                .clickable {
                    onSendMessageClick(text, false)
                    text = ""
                }
                .padding(defaultPadding),
            painter = painterResource(id = R.drawable.ic_outline_send_24),
            contentDescription = "Send"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainContent(
        state = AppState(
            blogMessages = listOf(
                BlogMessage()
            ),
            secretBlogsState = SecretBlogsState.VISIBLE,
            isEdit = false
        )
    )
}