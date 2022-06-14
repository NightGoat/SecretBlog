package ru.nightgoat.secretblog.providers

actual class DbPathProvider actual constructor() {

    actual fun provideDbPath(): String {
        return ContextProvider().requireContext().dataDir.path
    }
}