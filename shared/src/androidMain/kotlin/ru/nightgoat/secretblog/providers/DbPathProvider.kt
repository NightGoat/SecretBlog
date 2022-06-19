package ru.nightgoat.secretblog.providers

actual class DbPathProvider actual constructor() {

    actual fun provide(): String {
        return ContextProvider().requireContext().dataDir.path
    }
}