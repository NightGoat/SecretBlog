package ru.nightgoat.secretblog.core

import org.reduxkotlin.Reducer

val reducer: Reducer<AppState> = { state, action ->
    rootReducer(state, action)
}

fun rootReducer(state: AppState, action: Any) = AppState(
    blogMessages = blogMessagesReducer(state.blogMessages, action = action),
    secretBlogsState = secretBlogStateReducer(state.secretBlogsState, action)
)
