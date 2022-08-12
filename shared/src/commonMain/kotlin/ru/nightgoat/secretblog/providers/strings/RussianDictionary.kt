package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.models.ThemeType

object RussianDictionary : Dictionary {
    //Common
    override val yes: String by lazy { "Да" }
    override val no by lazy { "Нет" }
    override val back by lazy { "Назад" }

    //Pin
    override val wrongPincode by lazy { "Неверный пинкод!" }
    override val cannotRememberPin: String by lazy { "Я не помню пинкод" }
    override val eraseAppDataAlertTitle by lazy { "Внимание!" }
    override val eraseAppDataAlertMessage by lazy {
        "Это действие удалит всю информацию в приложении " +
                "и пропустит вас через пинкод, вы желаете продолжить?"
    }

    //Settings
    override val settingsTitle: String by lazy { "Настройки" }
    override val settingsPincodeOnEnterCheckBox: String by lazy { "Проверка пинкода при входе" }
    override val settingsPincodeSecretVisibilityCheckBox: String by lazy { "Проверка пинкода при отображении секретных сообщений" }
    override val settingsPincodeSettingsCheckBox: String by lazy { "Проверка пинкода для настроек" }
    override val deleteAllMessages: String by lazy { "Удалить все сообщения" }
    override val deleteAllMessagesAlertTitle: String by lazy { "Внимание!" }
    override val deleteAllMessagesAlertMessage: String by lazy {
        "Это действие удалит все сообщения!" +
                "Вы желаете продолжить?"
    }
    override val theme: String by lazy {
        "Тема"
    }

    //Chat
    override val messageTextPlaceHolder: String by lazy { "Текст" }
    override val copy: String by lazy { "Скопировать" }
    override val edit: String by lazy { "Изменить" }
    override val delete by lazy { "Удалить" }
    override val makeSecret: String by lazy { "Спрятать" }
    override val revealMessage: String by lazy { "Открыть" }

    override fun mapThemeName(theme: ThemeType): String {
        return when (theme) {
            ThemeType.Dark -> "Темная"
            ThemeType.Light -> "Светлая"
            ThemeType.System -> "Как в системе"
        }
    }
}