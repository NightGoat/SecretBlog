package ru.nightgoat.secretblog.providers

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual class DbPathProvider {

    actual fun provideDbPath(): String {
        return NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )[0] as String
    }
}