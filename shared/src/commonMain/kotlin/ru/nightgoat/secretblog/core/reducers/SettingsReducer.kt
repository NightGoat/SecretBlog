package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.*
import ru.nightgoat.secretblog.core.action.SettingsAction

fun StoreViewModel.settingsReducer(
    action: SettingsAction,
    oldState: AppState
) {
    when (action) {
        SettingsAction.TurnOffPincode -> {
            settingsProvider.clearPincode()
            state.value = oldState.setPincode(false)
        }
        SettingsAction.ClearAllMessages -> {
            launch {
                sideEffect.emit(BlogEffect.DeleteAllMessagesDialog)
            }
        }
        SettingsAction.TurnOffOnSecretVisibilityPin -> {
            settingsProvider.isPinOnSecretVisibilitySet = false
            state.value = oldState.setVisibilityPincode(false)
        }
        SettingsAction.TurnOnOnSecretVisibilityPin -> {
            settingsProvider.isPinOnSecretVisibilitySet = true
            state.value = oldState.setVisibilityPincode(true)
        }
    }

}