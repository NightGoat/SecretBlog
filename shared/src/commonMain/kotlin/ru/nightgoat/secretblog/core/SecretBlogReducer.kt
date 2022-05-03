package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.SecretBlogsState

fun secretBlogStateReducer(state: SecretBlogsState, action: BlogAction) =
    when (action) {
        BlogAction.ReverseSecretBlogsVisibility -> state.reverse()
        else -> state
    }