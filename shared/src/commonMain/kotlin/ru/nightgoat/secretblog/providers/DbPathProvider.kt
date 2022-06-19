package ru.nightgoat.secretblog.providers

expect class DbPathProvider() {
    fun provide(): String
}