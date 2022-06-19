package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.SecretBlogsState

fun AppState.setPincode(isSet: Boolean) = this.copy(
    settings = this.settings.copy(isPinCodeSet = isSet)
)

fun AppState.setVisibilityPincode(isSet: Boolean) = this.copy(
    settings = this.settings.copy(isPinOnSecretVisibilitySet = isSet)
)

fun AppState.turnOffEditMode() = this.copy(
    isEdit = false,
    blogMessages = blogMessages.map {
        it.copy(isSelected = false)
    }
)

fun AppState.hideSecretMessages() = this.copy(
    secretBlogsState = SecretBlogsState.HIDDEN
)