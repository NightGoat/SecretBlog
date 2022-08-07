package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.BlogMessage

sealed class BlogEffect : Effect {
    object Empty : BlogEffect()
    object ScrollToLastElement : BlogEffect()
    object LoadSuccess : BlogEffect()
    object LogOut : BlogEffect()
    data class CopyToClipBoard(val text: String) : BlogEffect()
    data class EditMessage(val message: BlogMessage) : BlogEffect()
    data class PincodeCheckResult(
        val isPincodeRight: Boolean
    ) : BlogEffect()

    object CannotRememberPinCodeDialog : BlogEffect()
    object DropEnteredPincodeToEmpty : BlogEffect()
    object DeleteAllMessagesDialog : BlogEffect()

    data class Error(val error: Exception) : BlogEffect()
    data class Toast(
        val text: String
    ) : BlogEffect()

    data class Navigate(
        val route: String,
        val argument: String? = null,
        val clearCurrentScreenFromBackStack: Boolean = false
    ) : BlogEffect()

    object NavigateBack : BlogEffect()
    object ClearBackStackAndGoToChat : BlogEffect()
}