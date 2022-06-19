package ru.nightgoat.secretblog.providers.strings

import platform.Foundation.NSLocale
import platform.Foundation.languageCode

actual class LocaleProvider {
    actual fun provide(): String {
        return NSLocale().languageCode
    }
}