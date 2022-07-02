package ru.nightgoat.secretblog.models

enum class ThemeType(name: String) {
    System("System"), Dark("Dark"), Light("Light");

    companion object {
        fun getFromName(name: String) = when (name) {
            Dark.name -> Dark
            Light.name -> Light
            else -> System
        }
    }
}