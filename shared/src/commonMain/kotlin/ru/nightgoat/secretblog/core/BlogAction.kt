package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage

sealed class BlogAction : Action {
    data class AddMessage(val message: String, val isSecret: Boolean) : BlogAction()
    data class RemoveMessage(val message: BlogMessage) : BlogAction()
    object Refresh : BlogAction()
    object Start : BlogAction()
    object ReverseSecretBlogsVisibility : BlogAction()
}