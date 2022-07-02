package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action

sealed class SettingsAction : Action {
    object TurnOffPincode : SettingsAction()
    object ClearAllMessages : SettingsAction()
    object TurnOnOnSecretVisibilityPin : SettingsAction()
    object TurnOffOnSecretVisibilityPin : SettingsAction()
    data class SelectTheme(val themeName: String) : SettingsAction()
}
