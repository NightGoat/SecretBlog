package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.models.SecretBlogsState

data class AppState(
    val blogMessages: List<BlogMessage> = listOf(),
    val secretBlogsState: SecretBlogsState = SecretBlogsState.HIDDEN
) : State {
    val visibleMessages
        get() = when (secretBlogsState) {
            SecretBlogsState.HIDDEN -> blogMessages.filter { !it.isSecret }
            SecretBlogsState.VISIBLE -> blogMessages
        }

    val reversedVisibilty
        get() = this.copy(secretBlogsState = secretBlogsState.reverse())
}
