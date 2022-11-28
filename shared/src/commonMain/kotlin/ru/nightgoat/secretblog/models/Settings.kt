package ru.nightgoat.secretblog.models

data class Settings(
    val isPinCodeSet: Boolean = false,
    val isPinOnSecretVisibilitySet: Boolean = false,
    val isPinOnSettingsSet: Boolean = false,
    val isSendToTwitterFeatureOn: Boolean = false,
    val themeType: ThemeType = ThemeType.System
)
