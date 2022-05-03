package ru.nightgoat.secretblog.models

enum class SecretBlogsState {
    VISIBLE, HIDDEN;

    fun reverse() = when (this) {
        VISIBLE -> HIDDEN
        else -> VISIBLE
    }
}