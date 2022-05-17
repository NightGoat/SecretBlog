package ru.nightgoat.secretblog.data

expect class DbPathProvider() {
    fun provideDbPath(): String
}