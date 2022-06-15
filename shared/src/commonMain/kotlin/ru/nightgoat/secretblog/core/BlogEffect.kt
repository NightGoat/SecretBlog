package ru.nightgoat.secretblog.core

sealed class BlogEffect : Effect {
    object Empty : BlogEffect()
    object ScrollToLastElement : BlogEffect()
    object LoadSuccess : BlogEffect()
    object LogOut : BlogEffect()
    data class PincodeCheckResult(
        val isPincodeRight: Boolean
    ) : BlogEffect()

    data class Error(val error: Exception) : BlogEffect()
    data class Toast(
        val text: String
    ) : BlogEffect()

    data class Navigate(
        val route: String,
        val argument: String? = null
    ) : BlogEffect()

    object NavigateBack : BlogEffect()
}