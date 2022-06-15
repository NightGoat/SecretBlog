package ru.nightgoat.secretblog.core.reducers

import io.github.aakira.napier.Napier
import ru.nightgoat.secretblog.core.Action
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.PinCodeAction
import ru.nightgoat.secretblog.core.action.SettingsAction

fun StoreViewModel.mainReducer(action: Action) {
    Napier.d("Action: $action")
    val oldState = state.value
    when (action) {
        is GlobalAction -> globalActionReducer(action, oldState)
        is BlogAction -> blogActionReducer(action, oldState)
        is SettingsAction -> settingsReducer(action, oldState)
        is PinCodeAction -> pincodeReducer(action, oldState)
    }
}