package ru.nightgoat.secretblog.android.presentation.screens.base

sealed class Screen {
    abstract val route: String

    object Splash : Screen() {
        override val route = SPLASH_ROUTE
    }

    object PinCode : Screen() {
        const val IS_PINCODE_CHECK_ARG = "isPincodeCheck"
        const val IS_PINCODE_CHECK = "1"
        const val IS_PINCODE_SET = "0"
        override val route = PIN_CODE_ROUTE
        val routeWithCheck = "$route/$IS_PINCODE_CHECK"
        val routeWithSet = "$route/$IS_PINCODE_SET"
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
