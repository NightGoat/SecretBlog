package ru.nightgoat.secretblog.providers

interface Provider<T> {
    fun provide(): T
}