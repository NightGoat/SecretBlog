package ru.nightgoat.secretblog.providers.strings

import ru.nightgoat.secretblog.providers.Provider

class StringProvider : Provider<Dictionary> {

    private val locale by lazy {
        LocaleProvider().provide()
    }

    override fun provide(): Dictionary {
        return when (locale) {
            else -> EnglishDictionary
        }
    }
}