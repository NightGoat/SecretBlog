package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction

fun StoreViewModel.globalActionReducer(action: GlobalAction, oldState: AppState) {
    when (action) {
        is GlobalAction.Navigate -> {
            reduceSideEffect(BlogEffect.Navigate(action.route, action.argument))
        }
        is GlobalAction.NavigateBack -> {
            reduceSideEffect(BlogEffect.NavigateBack)
        }
        is GlobalAction.AppPaused -> {
            if (oldState.settings.isPinCodeSet) {
                sideEffect.tryEmit(BlogEffect.LogOut)
            }
        }
        is GlobalAction.AppResumed -> {
            launch {
                val all = dataBase.init()
                if (state.value.blogMessages.size != all.size) {
                    state.value = oldState.copy(
                        blogMessages = all,
                        settings = settingsProvider.settings
                    )
                }
            }
        }
        is GlobalAction.ClearApp -> {
            launch {
                dataBase.deleteAll()
                settingsProvider.clearPincode()
                state.value = AppState()
                reduceSideEffect(BlogEffect.ClearBackStackAndGoToChat)
            }
        }
    }
}