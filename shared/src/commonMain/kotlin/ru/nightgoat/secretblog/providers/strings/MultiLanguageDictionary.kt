package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.models.ThemeType

class MultiLanguageDictionary(private val lang: AppLanguage = AppLanguage.ENG) : Dictionary {

    private fun stringList(vararg strings: String) = lazy {
        require(strings.size <= AppLanguage.values().size && strings.isNotEmpty())
        strings.mapIndexed { index, string ->
            AppLanguage.values().getOrElse(index) { AppLanguage.ENG } to string
        }.toMap()[lang] ?: strings.first()
    }

    override val yes: String by stringList(
        "Yes",
        "Да"
    )
    override val no: String by stringList(
        "No",
        "Нет"
    )
    override val back by stringList(
        "Back",
        "Назад"
    )

    //Pin
    override val wrongPincode by stringList(
        "Wrong pin!",
        "Неверный пинкод!"
    )
    override val cannotRememberPin: String by stringList(
        "I cannot remember pin",
        "Я не помню пинкод"
    )
    override val eraseAppDataAlertTitle by stringList(
        "Warning!",
        "Внимание!"
    )
    override val eraseAppDataAlertMessage by stringList(
        "This action will erase all data from app and let you in through pincode, do you wish to continue?",
        "Это действие удалит всю информацию в приложении и пропустит вас через пинкод, вы желаете продолжить?"
    )

    //Settings
    override val settingsTitle: String by stringList(
        "Settings",
        "Настройки"
    )
    override val settingsPincodeOnEnterCheckBox: String by stringList(
        "Pin on login",
        "Проверка пинкода при входе"
    )
    override val settingsPincodeSecretVisibilityCheckBox: String by stringList(
        "Pin on secret messages visibility",
        "Проверка пинкода при отображении секретных сообщений"
    )
    override val settingsPincodeSettingsCheckBox: String by stringList(
        "Pin on settings",
        "Проверка пинкода для настроек"
    )
    override val deleteAllMessages: String by stringList(
        "Delete all messages",
        "Удалить все сообщения"
    )
    override val deleteAllMessagesAlertTitle: String by stringList(
        "Warning!",
        "Внимание!"
    )
    override val deleteAllMessagesAlertMessage: String by stringList(
        "This action will erase all messages, do you wish to continue?",
        "Это действие удалит все сообщения! Вы желаете продолжить?"
    )
    override val theme: String by stringList(
        "Theme",
        "Тема"
    )
    override val twitterSetting: String by stringList(
        "Send to Twitter action",
        "Предлагать отправить в Twitter"
    )

    //Chat
    override val messageTextPlaceHolder: String by stringList(
        "Message",
        "Текст"
    )
    override val copy: String by stringList(
        "Copy",
        "Скопировать"
    )
    override val edit: String by stringList(
        "Edit",
        "Изменить"
    )
    override val delete by stringList(
        "Delete",
        "Удалить"
    )
    override val makeSecret: String by stringList(
        "Make secret",
        "Спрятать"
    )
    override val revealMessage: String by stringList(
        "Reveal",
        "Открыть"
    )
    override val sendToTwitter: String by stringList(
        "Send to Twitter",
        "Отправить в Twitter"
    )

    override fun mapThemeName(theme: ThemeType): String {
        return when (lang) {
            AppLanguage.ENG -> theme.name
            AppLanguage.RU -> RussianDictionary.mapThemeName(theme)
        }
    }
}

enum class AppLanguage {
    ENG, RU
}