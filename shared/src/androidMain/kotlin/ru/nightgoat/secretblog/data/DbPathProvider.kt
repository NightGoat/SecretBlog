package ru.nightgoat.secretblog.data


actual class DbPathProvider actual constructor() {

    actual fun provideDbPath(): String {
        return ContextProvider().requireContext().dataDir.path
    }
}