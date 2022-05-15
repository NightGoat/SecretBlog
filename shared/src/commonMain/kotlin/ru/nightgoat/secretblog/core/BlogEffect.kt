package ru.nightgoat.secretblog.core

sealed class BlogEffect : Effect {
    object Empty : BlogEffect()
    object ScrollToLastElement : BlogEffect()
    data class Error(val error: Exception) : BlogEffect()
}