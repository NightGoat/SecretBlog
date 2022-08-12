package ru.nightgoat.secretblog.core

import ru.nightgoat.secretblog.models.ChatMessagesEditMode
import ru.nightgoat.secretblog.models.SecretBlogsState
import ru.nightgoat.secretblog.models.ThemeType

fun AppState.setPincode(isSet: Boolean) = this.changeSettings(
    isPinCodeSet = isSet,
    isPinOnSettingsSet = false,
    isPinOnSecretVisibilitySet = false
)

fun AppState.setVisibilityPincode(isSet: Boolean) =
    this.changeSettings(isPinOnSecretVisibilitySet = isSet)

fun AppState.turnOffEditMode() = this.copy(
    editMode = ChatMessagesEditMode.None,
    blogMessages = blogMessages.map {
        it.copy(isSelected = false)
    }
)

fun AppState.hideSecretMessages() = this.copy(
    secretBlogsState = SecretBlogsState.HIDDEN
)

fun AppState.changeSettings(
    isPinCodeSet: Boolean = this.settings.isPinCodeSet,
    isPinOnSecretVisibilitySet: Boolean = this.settings.isPinOnSecretVisibilitySet,
    isPinOnSettingsSet: Boolean = this.settings.isPinOnSettingsSet,
    themeType: ThemeType = this.settings.themeType
) = this.copy(
    settings = this.settings.copy(
        isPinCodeSet = isPinCodeSet,
        isPinOnSecretVisibilitySet = isPinOnSecretVisibilitySet,
        isPinOnSettingsSet = isPinOnSettingsSet,
        themeType = themeType
    )
)