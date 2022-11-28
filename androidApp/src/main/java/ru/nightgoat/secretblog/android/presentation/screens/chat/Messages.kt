package ru.nightgoat.secretblog.android.presentation.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.dropdownIconSize
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.ChatMessagesEditMode
import ru.nightgoat.secretblog.models.SecretBlogsState
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary


private val MESSAGE_TIME_STAMP_TEXT_SIZE = 12.sp
private val MESSAGE_TEXT_SIZE = 16.sp
private val selectedMessageBorder = 2.dp
private val dropDownMinWidth = 128.dp

@Composable
fun Messages(
    modifier: Modifier = Modifier,
    dictionary: Dictionary = EnglishDictionary,
    listState: LazyListState,
    state: AppState,
    onDropDownSelected: (MessagesDropdowns.MessageDropDownSelectables, BlogMessage) -> Unit = { _, _ -> },
    onLongPress: () -> Unit,
    onMessageSelect: (BlogMessage, Boolean) -> Unit,
) {
    val dropdowns by remember { mutableStateOf(MessagesDropdowns(dictionary)) }
    val haptic = LocalHapticFeedback.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.End
    ) {
        items(state.visibleMessages) { message ->
            var isExpanded by remember { mutableStateOf(false) }
            var x by remember { mutableStateOf(0.dp) }
            var y by remember { mutableStateOf(0.dp) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (state.editMode == ChatMessagesEditMode.SelectForDelete) {
                                    onMessageSelect(message, !message.isSelected)
                                } else {
                                    x = it.x.toDp()
                                    y = (-it.y).toDp()
                                    isExpanded = !isExpanded
                                }
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onLongPress = {
                                onLongPress()
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = state.isModeSelectionForDelete,
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
                if (!state.isModeSelectionForDelete) {
                    SimpleSpacer()
                }
                MessageCard(
                    state = state,
                    message = message,
                    isExpanded = isExpanded,
                    offset = DpOffset(x, y),
                    onDissmisDropDown = {
                        isExpanded = !isExpanded
                    },
                    dropdowns = dropdowns.list(
                        message = message,
                        isTwitterButtonEnabled = state.settings.isSendToTwitterFeatureOn
                    ),
                    onDropDownSelected = onDropDownSelected,
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
    isExpanded: Boolean,
    offset: DpOffset,
    onDissmisDropDown: () -> Unit,
    dropdowns: List<MessagesDropdowns.MessageDropDownSelectables>,
    onDropDownSelected: (MessagesDropdowns.MessageDropDownSelectables, BlogMessage) -> Unit,
    onMessageSelect: (BlogMessage, Boolean) -> Unit,
    onLongPress: () -> Unit
) {
    var backgroundColor = AppColor.elephantBone
    var textColor = Color.Black
    var timeStampColor = Color.DarkGray
    var borderStroke: BorderStroke? = null
    if (message.isSecret) {
        backgroundColor = AppColor.blue
        timeStampColor = Color.Black
        textColor = Color.White
    }
    val editMode = state.editMode
    if (isExpanded || (editMode is ChatMessagesEditMode.Edit && editMode.message.id == message.id)) {
        val selectionColor = if (message.isSecret) {
            AppColor.elephantBone
        } else {
            MaterialTheme.colors.primary
        }
        borderStroke =
            BorderStroke(width = selectedMessageBorder, color = selectionColor)
    }

    Card(
        modifier = Modifier
            .padding(defaultPadding),
        backgroundColor = backgroundColor,
        border = borderStroke
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
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDissmisDropDown,
            offset = offset
        ) {
            dropdowns.forEach { selection ->
                DropdownMenuItem(onClick = {
                    onDropDownSelected(selection, message)
                    onDissmisDropDown()
                }) {
                    Row(
                        modifier = Modifier.defaultMinSize(minWidth = dropDownMinWidth)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(dropdownIconSize)
                                .padding(end = defaultPadding),
                            painter = painterResource(id = selection.iconId),
                            contentDescription = selection.title
                        )
                        Text(text = selection.title)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesPreview() {
    BlogTheme {
        Messages(
            listState = LazyListState(),
            state = AppState(
                blogMessages = listOf(
                    BlogMessage().apply {
                        text = "Hello world"
                        isSecret = true
                    }, BlogMessage().apply {
                        text = "Indeed hello world"
                    }
                ),
                secretBlogsState = SecretBlogsState.VISIBLE,
            ),
            onLongPress = { },
            onMessageSelect = { _, _ -> }
        )
    }
}