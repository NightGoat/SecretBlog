package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.models.ThemeType

@Deprecated(
    "Use MultiLanguageDictionary",
    replaceWith = ReplaceWith("MultiLanguageDictionary()")
)
object EnglishDictionary {
    //Common
    val yes: String by lazy { "Yes" }
    val no by lazy { "No" }
    val back by lazy { "Back" }

    //Pin
    val wrongPincode by lazy { "Wrong pin!" }
    val cannotRememberPin: String by lazy { "I cannot remember pin" }
    val eraseAppDataAlertTitle by lazy { "Warning!" }
    val eraseAppDataAlertMessage by lazy {
        "This action will erase all data from app " +
                "and let you in through pincode, do you wish to continue?"
    }

    //Settings
    val settingsTitle: String by lazy { "Settings" }
    val settingsPincodeOnEnterCheckBox: String by lazy { "Pin on login" }
    val settingsPincodeSecretVisibilityCheckBox: String by lazy { "Pin on secret messages visibility" }
    val settingsPincodeSettingsCheckBox: String by lazy { "Pin on settings" }
    val deleteAllMessages: String by lazy { "Delete all messages" }
    val deleteAllMessagesAlertTitle: String by lazy { "Warning!" }
    val deleteAllMessagesAlertMessage: String by lazy {
        "This action will erase all " +
                "messages, do you wish to continue?"
    }
    val theme: String by lazy {
        "Theme"
    }
    val twitterSetting: String by lazyString("Send to Twitter action")

    //Chat
    val messageTextPlaceHolder: String by lazy { "Message" }
    val copy: String by lazy { "Copy" }
    val edit: String by lazy { "Edit" }
    val delete by lazy { "Delete" }
    val makeSecret: String by lazy { "Make secret" }
    val revealMessage: String by lazy { "Reveal" }
    val sendToTwitter: String by lazyString("Send to Twitter")

    fun mapThemeName(theme: ThemeType): String {
        return theme.name
    }
}