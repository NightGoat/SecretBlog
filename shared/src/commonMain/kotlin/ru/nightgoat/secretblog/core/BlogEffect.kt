package ru.nightgoat.secretblog.core

sealed class BlogEffect : Effect {
    data class Error(val error: Exception) : BlogEffect()
}