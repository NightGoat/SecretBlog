package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.models.ThemeType

object EnglishDictionary : Dictionary {
    //Common
    override val yes: String by lazy { "Yes" }
    override val no by lazy { "No" }
    override val back by lazy { "Back" }

    //Pin
    override val wrongPincode by lazy { "Wrong pin!" }
    override val cannotRememberPin: String by lazy { "I cannot remember pin" }
    override val eraseAppDataAlertTitle by lazy { "Warning!" }
    override val eraseAppDataAlertMessage by lazy {
        "This action will erase all data from app " +
                "and let you in through pincode, do you wish to continue?"
    }

    //Settings
    override val settingsTitle: String by lazy { "Settings" }
    override val settingsPincodeOnEnterCheckBox: String by lazy { "Pin on login" }
    override val settingsPincodeSecretVisibilityCheckBox: String by lazy { "Pin on secret messages visibility" }
    override val deleteAllMessages: String by lazy { "Delete all messages" }
    override val deleteAllMessagesAlertTitle: String by lazy { "Warning!" }
    override val deleteAllMessagesAlertMessage: String by lazy {
        "This action will erase all " +
                "messages, do you wish to continue?"
    }
    override val theme: String by lazy {
        "Theme"
    }

    //Chat
    override val messageTextPlaceHolder: String by lazy { "Message" }
    override val copy: String by lazy { "Copy" }
    override val edit: String by lazy { "Edit" }
    override val delete by lazy { "Delete" }

    override fun mapThemeName(theme: ThemeType): String {
        return theme.name
    }
}