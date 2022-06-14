package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction

fun StoreViewModel.globalActionReducer(action: GlobalAction, oldState: AppState) {
    when (action) {
        is GlobalAction.Navigate -> {
            launch {
                sideEffect.emit(BlogEffect.Navigate(action.route, action.argument))
            }
        }
    }
}