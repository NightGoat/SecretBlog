package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.SecretBlogsState

data class AppState(
    val blogMessages: List<BlogMessage> = listOf(),
    val secretBlogsState: SecretBlogsState = SecretBlogsState.HIDDEN,
    val isEdit: Boolean = false,
) : State {
    val visibleMessages
        get() = when (secretBlogsState) {
            SecretBlogsState.HIDDEN -> blogMessages.filter { !it.isSecret }
            SecretBlogsState.VISIBLE -> blogMessages
        }

    val visibleMessagesStrings
        get() = visibleMessages.map { it.text }

    val reversedVisibility
        get() = this.copy(secretBlogsState = secretBlogsState.reverse())

    val reversedEdit
        get() = this.copy(blogMessages = this.blogMessages.map {
            it.copy(isSelected = false)
        }, isEdit = !this.isEdit)

    val sizeOfSelectedMessages
        get() = this.blogMessages.count { it.isSelected }.toString()
}
