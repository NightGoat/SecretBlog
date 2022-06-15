package ru.nightgoat.secretblog.core

fun AppState.setPincode(isSet: Boolean) = this.copy(
    settings = this.settings.copy(isPinCodeSet = isSet)
)

fun AppState.turnOffEditMode() = this.copy(
    isEdit = false,
    blogMessages = blogMessages.map {
        it.copy(isSelected = false)
    }
)