package ru.nightgoat.secretblog.providers.strings

import java.util.*

actual class LocaleProvider {
    actual fun provide(): String {
        return Locale.getDefault().language
    }
}