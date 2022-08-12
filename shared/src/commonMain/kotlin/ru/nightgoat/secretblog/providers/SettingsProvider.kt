package ru.nightgoat.secretblog.providers

import ru.nightgoat.secretblog.models.Settings
import ru.nightgoat.secretblog.models.ThemeType

class SettingsProvider {
    private val kvault = KvaultProvider().provide()

    val settings: Settings
        get() = Settings(
            isPinCodeSet = isPinCodeSet,
            isPinOnSecretVisibilitySet = isPinOnSecretVisibilitySet,
            themeType = ThemeType.getFromName(selectedTheme)
        )

    private var pinCode: String by kvault.stringPref()
    var isPinCodeSet: Boolean by kvault.booleanPref()
    var isPinOnSecretVisibilitySet: Boolean by kvault.booleanPref()
    var selectedTheme: String by kvault.stringPref()

    fun setNewPincode(newPincode: String) {
        pinCode = newPincode
        isPinCodeSet = true
    }

    fun clearPincode() {
        isPinCodeSet = false
        pinCode = ""
    }

    fun isPinCodeCorrect(newPincode: String): Boolean {
        return pinCode == newPincode
    }

    companion object {
        const val PIN_CODE_KEY = "PIN_CODE_KEY"
        const val THEME_KEY = "THEME_KEY"
        const val IS_PIN_CODE_SET_KEY = "IS_PINC_ODE_SET_KEY"
        const val IS_PIN_CODE_ON_SECRET_VISIBILITY_SET_KEY =
            "IS_PIN_CODE_ON_SECRET_VISIBILITY_SET_KEY"
    }
}