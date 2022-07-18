package ru.nightgoat.secretblog.models

fun BlogMessage?.orEmpty() = this ?: BlogMessage()