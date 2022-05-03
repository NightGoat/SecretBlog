package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage

fun blogMessagesReducer(state: List<BlogMessage>, action: BlogAction) =
    when (action) {
        is BlogAction.addMessage -> state + BlogMessage(
            id = state.lastIndex + 1,
            text = action.message,
            isSecret = action.isSecret
        )
        is BlogAction.removeMessage -> state.mapNotNull {
            it.takeIf { it.id != action.id }
        }
        else -> state
    }