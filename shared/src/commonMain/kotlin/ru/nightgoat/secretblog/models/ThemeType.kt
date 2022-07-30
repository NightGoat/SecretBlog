package ru.nightgoat.secretblog.models

enum class ThemeType(name: String) {
    System("System"), Dark("Dark"), Light("Light");

    companion object {
        fun getFromOrdinal(ordinal: Int) = when (ordinal) {
            Dark.ordinal -> Dark
            Light.ordinal -> Light
            else -> System
        }
    }
}