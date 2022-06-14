package ru.nightgoat.secretblog.core

fun AppState.setPincode(isSet: Boolean) = this.copy(
    settings = this.settings.copy(isPinCodeSet = isSet)
)