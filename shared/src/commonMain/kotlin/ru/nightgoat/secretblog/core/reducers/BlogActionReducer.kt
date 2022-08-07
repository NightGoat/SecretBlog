package ru.nightgoat.secretblog.core.reducers

import io.github.nightgoat.kexcore.changeElementBy
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.*
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.RefreshAction
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.ChatMessagesEditMode

fun StoreViewModel.blogActionReducer(action: BlogAction, oldState: AppState) {
    when (action) {
        is BlogAction.Start -> reduceStartAction(oldState)
        is BlogAction.Refresh -> {
            launch(CoroutineName("Store.Refresh")) {
                state.value = reduceRefreshAction(action, oldState)
                sideEffect.emit(BlogEffect.ScrollToLastElement)
            }
        }
        is BlogAction.AddMessage -> {
            launch(CoroutineName("Store.AddMessage")) {
                val newMessage = BlogMessage.newInstance(
                    text = action.message,
                    isSecret = action.isSecret
                )
                addMessage(newMessage)
            }
        }
        is BlogAction.ClearDB -> {
            launch(CoroutineName("Store.ClearDB")) {
                dataBase.deleteAll()
                refresh(action = RefreshAction.DeleteAll)
            }
        }
        is BlogAction.RemoveMessages -> {
            val messages = action.messages.toList()
            messages.forEach { message ->
                launch(CoroutineName("Store.RemoveMessages")) {
                    dataBase.delete(message)
                }
            }

            refresh(RefreshAction.Delete(messages))
        }
        is BlogAction.ReverseSecretBlogsVisibility -> {
            state.value = oldState.reversedVisibility
        }
        is BlogAction.ReverseEditMode -> {
            state.value = oldState.reversedEdit
        }
        is BlogAction.SelectMessage -> {
            state.value = oldState.copy(
                blogMessages = oldState.blogMessages.map { message ->
                    if (message.id == action.message.id) {
                        message.copy(isSelected = action.isSelected)
                    } else {
                        message
                    }
                }
            )
        }
        is BlogAction.CopyToClipBoard -> {
            reduceSideEffect(BlogEffect.CopyToClipBoard(action.text), BlogEffect.Toast("Copied!"))
        }
        is BlogAction.StartEditMessage -> {
            reduceSideEffect(BlogEffect.EditMessage(action.message))
            state.value = oldState.copy(
                editMode = ChatMessagesEditMode.Edit(action.message)
            )
        }
        is BlogAction.CancelEditMessage -> {
            reduceSideEffect(BlogEffect.EditMessage(BlogMessage()))
            state.value = oldState.copy(
                editMode = ChatMessagesEditMode.None
            )
        }
        is BlogAction.EndEditMessage -> {
            launch {
                sideEffect.emit(BlogEffect.EditMessage(BlogMessage()))
                dataBase.update(action.message)
                state.value = oldState.copy(
                    blogMessages = oldState.blogMessages.changeElementBy(action.message) {
                        it.id == action.message.id
                    },
                    editMode = ChatMessagesEditMode.None
                )
            }
        }
        is BlogAction.OpenSettingsScreen -> {
            var argument: String? = null
            val screen = if (oldState.settings.isPinOnSettingsSet) {
                argument = Screen.PinCode.IS_PINCODE_CHECK_ON_SETTINGS
                Screen.PinCode.route
            } else {
                Screen.Settings.route
            }
            dispatch(GlobalAction.Navigate(screen, argument))
        }
    }
}

private fun StoreViewModel.reduceStartAction(oldState: AppState) {
    launch(CoroutineName("Store.Start")) {
        val newMessages = dataBase.init()
        state.value = oldState.copy(
            blogMessages = newMessages,
            settings = settingsProvider.settings
        )
        sideEffect.emit(BlogEffect.LoadSuccess)
    }
}

private fun reduceRefreshAction(
    action: BlogAction.Refresh,
    oldState: AppState,
): AppState {
    val oldMessages = oldState.blogMessages
    return when (val refreshAction = action.refreshAction) {
        is RefreshAction.Add -> {
            oldState.copy(blogMessages = oldMessages + refreshAction.message)
        }
        is RefreshAction.Delete -> {
            oldState.copy(blogMessages = oldMessages - refreshAction.messages.toSet())
        }
        is RefreshAction.DeleteAll -> {
            oldState.copy(blogMessages = emptyList())
        }
    }.turnOffEditMode()
}