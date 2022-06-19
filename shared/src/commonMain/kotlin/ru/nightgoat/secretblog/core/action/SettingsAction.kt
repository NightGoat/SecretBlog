package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action

sealed class SettingsAction : Action {
    object TurnOffPincode : SettingsAction()
    object ClearAllMessages : SettingsAction()
}
