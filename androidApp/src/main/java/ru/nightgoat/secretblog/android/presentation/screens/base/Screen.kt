package ru.nightgoat.secretblog.android.presentation.screens.base

sealed class Screen {
    abstract val route: String

    object Splash : Screen() {
        override val route = SPLASH_ROUTE
    }

    object PinCode : Screen() {
        override val route = PIN_CODE_ROUTE
    }

    object Chat : Screen() {
        override val route = CHAT_ROUTE
    }

    object Settings : Screen() {
        override val route = SETTINGS_ROUTE
    }

    private companion object {
        const val SPLASH_ROUTE = "splash"
        const val PIN_CODE_ROUTE = "pincode"
        const val CHAT_ROUTE = "chat"
        const val SETTINGS_ROUTE = "settings"
    }
}
