package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.RefreshAction
import ru.nightgoat.secretblog.core.turnOffEditMode
import ru.nightgoat.secretblog.models.BlogMessage

fun StoreViewModel.blogActionReducer(action: BlogAction, oldState: AppState) {
    when (action) {
        is BlogAction.Start -> {
            launch(CoroutineName("Store.Start")) {
                val newMessages = dataBase.init()
                state.value = oldState.copy(
                    blogMessages = newMessages,
                    settings = settingsProvider.settings
                )
                sideEffect.emit(BlogEffect.ScrollToLastElement)
            }
        }
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