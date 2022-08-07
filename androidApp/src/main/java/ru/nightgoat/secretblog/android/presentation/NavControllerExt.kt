package ru.nightgoat.secretblog.android.presentation

import androidx.navigation.NavController
import ru.nightgoat.secretblog.core.Screen

fun NavController.navigate(screen: Screen) {
    navigate(screen.route)
}