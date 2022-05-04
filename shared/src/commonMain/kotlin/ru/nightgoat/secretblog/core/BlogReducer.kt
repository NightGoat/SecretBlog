package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.data.MessagesDataBase
import ru.nightgoat.secretblog.models.BlogMessage


fun blogMessagesReducer(state: List<BlogMessage>, action: Any): List<BlogMessage> {
    return when (action) {
        is BlogAction.AddMessage -> {
            val newMessage = BlogMessage(
                text = action.message,
                isSecret = action.isSecret
            )
            MessagesDataBase.add(newMessage)
            state + newMessage
        }
        is BlogAction.RemoveMessage -> {
            MessagesDataBase.delete(action.message.id)
            state.mapNotNull {
                it.takeIf { it.id != action.message.id }
            }
        }
        else -> state
    }
}