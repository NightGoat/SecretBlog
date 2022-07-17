package ru.nightgoat.secretblog.core

sealed class BlogEffect : Effect {
    object Empty : BlogEffect()
    object ScrollToLastElement : BlogEffect()
    object LoadSuccess : BlogEffect()
    object LogOut : BlogEffect()
    class CopyToClipBoard(val text: String) : BlogEffect()
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
        val argument: String? = null
    ) : BlogEffect()

    object NavigateBack : BlogEffect()
    object ClearBackStackAndGoToChat : BlogEffect()
}