package ru.nightgoat.secretblog.android.presentation

import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen

fun NavController.navigate(screen: Screen) {
    navigate(screen.route)
}