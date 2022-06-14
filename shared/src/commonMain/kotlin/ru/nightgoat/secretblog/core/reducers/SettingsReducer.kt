package ru.nightgoat.secretblog.core.reducers

import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.SettingsAction
import ru.nightgoat.secretblog.core.setPincode

fun StoreViewModel.settingsReducer(
    action: SettingsAction,
    oldState: AppState
) {
    when (action) {
        SettingsAction.TurnOffPincode -> {
            settingsProvider.clearPincode()
            state.value = oldState.setPincode(false)
        }
    }

}