package ru.nightgoat.secretblog.providers

import ru.nightgoat.secretblog.models.Settings

class SettingsProvider : KVaultPref() {
    override val kvault = KvaultProvider().provide()

    val settings: Settings
        get() = Settings(
            isPinCodeSet = isPinCodeSet
        )

    private var pinCode: String? by stringPref(PIN_CODE_KEY)
    var isPinCodeSet: Boolean by booleanPref(IS_PIN_CODE_SET_KEY)

    fun setNewPincode(newPincode: String) {
        pinCode = newPincode
        isPinCodeSet = true
    }

    fun clearPincode() {
        isPinCodeSet = false
        pinCode = null
    }

    fun isPinCodeCorrect(newPincode: String): Boolean {
        return pinCode == newPincode
    }

    companion object {
        const val PIN_CODE_KEY = "PIN_CODE_KEY"
        const val IS_PIN_CODE_SET_KEY = "IS_PINC_ODE_SET_KEY"
    }
}