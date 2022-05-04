package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage

sealed class BlogAction {
    class AddMessage(val message: String, val isSecret: Boolean) : BlogAction()
    class RemoveMessage(val message: BlogMessage) : BlogAction()

    object ReverseSecretBlogsVisibility : BlogAction()
}