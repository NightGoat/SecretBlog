package ru.nightgoat.secretblog.core

fun rootReducer(state: AppState, action: BlogAction) = AppState(
    blogMessages = blogMessagesReducer(state.blogMessages, action = action),
    secretBlogsState = secretBlogStateReducer(state.secretBlogsState, action)
)