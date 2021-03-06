package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action
import ru.nightgoat.secretblog.models.BlogMessage

sealed class BlogAction : Action {
    data class AddMessage(val message: String, val isSecret: Boolean) : BlogAction()
    data class RemoveMessages(val messages: List<BlogMessage>) : BlogAction()
    data class Refresh(
        val refreshAction: RefreshAction
    ) : BlogAction()

    object Start : BlogAction()
    object ReverseSecretBlogsVisibility : BlogAction()
    object ReverseEditMode : BlogAction()
    data class SelectMessage(val message: BlogMessage, val isSelected: Boolean) : BlogAction()
    object ClearDB : BlogAction()
    data class CopyToClipBoard(val text: String) : BlogAction()
    data class StartEditMessage(val message: BlogMessage) : BlogAction()
    object CancelEditMessage : BlogAction()
    data class EndEditMessage(val message: BlogMessage) : BlogAction()
}

sealed class RefreshAction {
    data class Add(val message: BlogMessage) : RefreshAction()
    data class Delete(val messages: List<BlogMessage>) : RefreshAction()
    object DeleteAll : RefreshAction()
}