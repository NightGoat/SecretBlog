package ru.nightgoat.secretblog.core.reducers

import kotlinx.coroutines.launch
import ru.nightgoat.secretblog.core.*
import ru.nightgoat.secretblog.core.action.SettingsAction
import ru.nightgoat.secretblog.models.ThemeType

fun StoreViewModel.settingsReducer(
    action: SettingsAction,
    oldState: AppState
) {
    when (action) {
        is SettingsAction.TurnOffPincode -> {
            settingsProvider.clearPincode()
            state.value = oldState.setPincode(false)
        }
        is SettingsAction.ClearAllMessages -> {
            launch {
                sideEffect.emit(BlogEffect.DeleteAllMessagesDialog)
            }
        }
        is SettingsAction.TurnOffOnSecretVisibilityPin -> {
            settingsProvider.isPinOnSecretVisibilitySet = false
            state.value = oldState.setVisibilityPincode(false)
        }
        is SettingsAction.TurnOnOnSecretVisibilityPin -> {
            settingsProvider.isPinOnSecretVisibilitySet = true
            state.value = oldState.setVisibilityPincode(true)
        }
        is SettingsAction.SelectTheme -> {
            val themeOrdinal = action.themeName.ordinal
            settingsProvider.selectedTheme = themeOrdinal
            state.value = state.value.changeSettings(
                themeType = ThemeType.getFromOrdinal(themeOrdinal)
            )
        }
        is SettingsAction.ChangeSettingsPinCheck -> {
            val isSet = action.isChecked
            settingsProvider.isPinOnSettingsSet = isSet
            state.value = oldState.changeSettings(
                isPinOnSettingsSet = isSet
            )
        }
        is SettingsAction.ChangeSettingsTwitterButton -> {
            val isSet = action.isChecked
            settingsProvider.isSendToTwitterFeatureOn = isSet
            state.value = oldState.changeSettings(
                isTwitterFeatureOn = isSet
            )
        }
    }

}