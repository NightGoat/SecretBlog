package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.models.ThemeType

@Deprecated(
    "Use MultiLanguageDictionary",
    replaceWith = ReplaceWith("MultiLanguageDictionary()")
)
object RussianDictionary {
    //Common
    val yes: String by lazy { "Да" }
    val no by lazy { "Нет" }
    val back by lazy { "Назад" }

    //Pin
    val wrongPincode by lazy { "Неверный пинкод!" }
    val cannotRememberPin: String by lazy { "Я не помню пинкод" }
    val eraseAppDataAlertTitle by lazy { "Внимание!" }
    val eraseAppDataAlertMessage by lazy {
        "Это действие удалит всю информацию в приложении " +
                "и пропустит вас через пинкод, вы желаете продолжить?"
    }

    //Settings
    val settingsTitle: String by lazy { "Настройки" }
    val settingsPincodeOnEnterCheckBox: String by lazy { "Проверка пинкода при входе" }
    val settingsPincodeSecretVisibilityCheckBox: String by lazy { "Проверка пинкода при отображении секретных сообщений" }
    val settingsPincodeSettingsCheckBox: String by lazy { "Проверка пинкода для настроек" }
    val deleteAllMessages: String by lazy { "Удалить все сообщения" }
    val deleteAllMessagesAlertTitle: String by lazy { "Внимание!" }
    val deleteAllMessagesAlertMessage: String by lazy {
        "Это действие удалит все сообщения!" +
                "Вы желаете продолжить?"
    }
    val theme: String by lazy {
        "Тема"
    }
    val twitterSetting: String by lazyString("Предлагать отправить в Twitter")

    //Chat
    val messageTextPlaceHolder: String by lazy { "Текст" }
    val copy: String by lazy { "Скопировать" }
    val edit: String by lazy { "Изменить" }
    val delete by lazy { "Удалить" }
    val makeSecret: String by lazy { "Спрятать" }
    val revealMessage: String by lazy { "Открыть" }
    val sendToTwitter: String by lazyString("Отправить в Twitter")

    fun mapThemeName(theme: ThemeType): String {
        return when (theme) {
            ThemeType.Dark -> "Темная"
            ThemeType.Light -> "Светлая"
            ThemeType.System -> "Как в системе"
        }
    }
}