package ru.nightgoat.secretblog.providers.strings

object EnglishDictionary : Dictionary {
    //Common
    override val yes: String by lazy { "Yes" }
    override val no by lazy { "No" }

    //Pin
    override val wrongPincode by lazy { "Wrong pin!" }
    override val cannotRememberPin: String by lazy { "I cannot remember pin" }
    override val eraseAppDataAlertTitle by lazy { "Warning!" }
    override val eraseAppDataAlertMessage by lazy { "This action will erase all data from app and let you in through pincode, do you wish to continue?" }

    //Settings
    override val settingsTitle: String by lazy { "Settings" }
    override val settingsPincodeOnEnterCheckBox: String by lazy { "Pin on login" }
}