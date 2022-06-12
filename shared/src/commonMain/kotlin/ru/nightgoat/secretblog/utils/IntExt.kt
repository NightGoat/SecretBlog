package ru.nightgoat.secretblog.utils

fun Int.addLeadingZero() = this.toString().padStart(2, '0')