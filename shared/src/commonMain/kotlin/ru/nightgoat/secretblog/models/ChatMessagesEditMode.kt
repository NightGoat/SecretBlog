package ru.nightgoat.secretblog.models

sealed class ChatMessagesEditMode {
    object None : ChatMessagesEditMode()
    object SelectForDelete : ChatMessagesEditMode()
    data class Edit(val message: BlogMessage) : ChatMessagesEditMode()
}