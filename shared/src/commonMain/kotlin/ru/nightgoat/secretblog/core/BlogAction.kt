package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage

sealed class BlogAction : Action {
    data class AddMessage(val message: String, val isSecret: Boolean) : BlogAction()
    data class RemoveMessages(val messages: List<BlogMessage>) : BlogAction()
    object Refresh : BlogAction()
    object Start : BlogAction()
    object ReverseSecretBlogsVisibility : BlogAction()
    object ReverseEditMode : BlogAction()
    data class SelectMessage(val message: BlogMessage, val isSelected: Boolean) : BlogAction()
    object ClearDB : BlogAction()
}