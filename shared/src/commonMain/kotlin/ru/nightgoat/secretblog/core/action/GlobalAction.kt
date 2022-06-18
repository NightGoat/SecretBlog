package ru.nightgoat.secretblog.core.action

import ru.nightgoat.secretblog.core.Action

sealed class GlobalAction : Action {
    data class Navigate(
        val route: String,
        val argument: String? = null
    ) : GlobalAction()

    object NavigateBack : GlobalAction()
    object AppPaused : GlobalAction()
    object AppResumed : GlobalAction()
    object ClearApp : GlobalAction()
}