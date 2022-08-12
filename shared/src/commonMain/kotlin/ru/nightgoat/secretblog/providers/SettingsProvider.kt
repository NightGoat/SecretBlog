package ru.nightgoat.secretblog.providers

import ru.nightgoat.secretblog.models.Settings
import ru.nightgoat.secretblog.models.ThemeType

class SettingsProvider {
    private val kvault = KvaultProvider().provide()

    val settings: Settings
        get() = Settings(
            isPinCodeSet = isPinCodeSet,
            isPinOnSecretVisibilitySet = isPinOnSecretVisibilitySet,
            isPinOnSettingsSet = isPinOnSettingsSet,
            themeType = ThemeType.getFromOrdinal(selectedTheme)
        )

    private var pinCode: String by kvault.stringPref()
    var isPinCodeSet: Boolean by kvault.booleanPref()
    var isPinOnSecretVisibilitySet: Boolean by kvault.booleanPref()
    var isPinOnSettingsSet: Boolean by kvault.booleanPref()
    var selectedTheme: Int by kvault.intPref()

    fun setNewPincode(newPincode: String) {
        pinCode = newPincode
        isPinCodeSet = true
    }

    fun clearPincode() {
        isPinCodeSet = false
        isPinOnSettingsSet = false
        isPinOnSecretVisibilitySet = false
        pinCode = ""
    }

    fun isPinCodeCorrect(newPincode: String): Boolean {
        return pinCode == newPincode
    }
}