package ru.nightgoat.secretblog.providers.strings

interface Dictionary {

    //Common
    val yes: String
    val no: String
    val back: String

    //Splash

    //Pincode
    val wrongPincode: String
    val cannotRememberPin: String
    val eraseAppDataAlertTitle: String
    val eraseAppDataAlertMessage: String

    //Settings
    val settingsTitle: String
    val settingsPincodeOnEnterCheckBox: String
    val settingsPincodeSecretVisibilityCheckBox: String
    val deleteAllMessages: String
    val deleteAllMessagesAlertTitle: String
    val deleteAllMessagesAlertMessage: String

    //Chat
    val messageTextPlaceHolder: String
    val edit: String
    val copy: String
}