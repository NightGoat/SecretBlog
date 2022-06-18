package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action

sealed class PinCodeAction : Action {
    data class SetPincode(
        val newPincode: String
    ) : PinCodeAction()

    data class CheckPincode(
        val pincodeToCheck: String
    ) : PinCodeAction()

    object CannotRememberPinCode : PinCodeAction()
}