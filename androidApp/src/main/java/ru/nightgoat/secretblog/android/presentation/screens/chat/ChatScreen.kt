package ru.nightgoat.secretblog.android.presentation.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.nightgoat.kexcore.orZero
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.AppIcon
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultElevation
import ru.nightgoat.secretblog.android.presentation.defaultPadding
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

private val TOOLBAR_TEXT_SIZE = 20.sp

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
        is BlogEffect.CopyToClipBoard -> {
            LocalClipboardManager.current.setText(AnnotatedString(effects.text))
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
        },
        onDropDownSelected = { dropDown, message ->
            when (dropDown) {
                is MessagesDropdowns.MessageDropDownSelectables.Copy -> {
                    viewModel.dispatch(BlogAction.CopyToClipBoard(message.text))
                }
                is MessagesDropdowns.MessageDropDownSelectables.Edit -> {
                    viewModel.dispatch(BlogAction.EditMessage(message))
                }
            }
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
    onSettingsClick: () -> Unit = {},
    onDropDownSelected: (MessagesDropdowns.MessageDropDownSelectables, BlogMessage) -> Unit = { _, _ -> }
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
            onLongPress = onLongPress,
            onDropDownSelected = onDropDownSelected
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