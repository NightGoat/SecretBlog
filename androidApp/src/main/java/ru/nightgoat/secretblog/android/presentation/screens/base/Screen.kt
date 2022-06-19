package ru.nightgoat.secretblog.android.presentation.screens.base

sealed class Screen {
    abstract val route: String

    object Splash : Screen() {
        override val route = SPLASH_ROUTE
    }

    object PinCode : Screen() {

        enum class State {
            SET, CHECK_ON_LOGIN, CHECK_ON_VISIBILITY, CHECK_ON_SETTINGS;

            fun isBackButtonVisible() = this != CHECK_ON_LOGIN

            companion object {
                fun fromArg(arg: String) = when (arg) {
                    IS_PINCODE_CHECK_ON_SETTINGS -> CHECK_ON_SETTINGS
                    IS_PINCODE_CHECK_ON_LOGIN -> CHECK_ON_LOGIN
                    IS_PINCODE_CHECK_ON_SECRET_VISIBILITY -> CHECK_ON_VISIBILITY
                    else -> SET
                }
            }
        }

        const val IS_PINCODE_CHECK_ARG = "isPincodeCheck"
        const val IS_PINCODE_CHECK_ON_SETTINGS = "4"
        const val IS_PINCODE_CHECK_ON_SECRET_VISIBILITY = "3"
        const val IS_PINCODE_CHECK_ON_LOGIN = "1"
        const val IS_PINCODE_SET = "0"
        override val route = PIN_CODE_ROUTE
        val routeWithCheck = "$route/$IS_PINCODE_CHECK_ON_LOGIN"
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
