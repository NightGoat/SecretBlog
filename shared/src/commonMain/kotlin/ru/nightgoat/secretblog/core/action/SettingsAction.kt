package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action
import ru.nightgoat.secretblog.models.ThemeType

sealed class SettingsAction : Action {
    object TurnOffPincode : SettingsAction()
    object ClearAllMessages : SettingsAction()
    object TurnOnOnSecretVisibilityPin : SettingsAction()
    object TurnOffOnSecretVisibilityPin : SettingsAction()
    data class ChangeSettingsPinCheck(val isChecked: Boolean) : SettingsAction()
    data class SelectTheme(val themeName: ThemeType) : SettingsAction()
    data class ChangeSettingsTwitterButton(val isChecked: Boolean) : SettingsAction()
}
