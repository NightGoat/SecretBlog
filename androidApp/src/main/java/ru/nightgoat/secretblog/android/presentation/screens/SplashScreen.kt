package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.GlobalAction

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    sideEffect: BlogEffect
) {
    viewModel.dispatch(BlogAction.Start)
    if (state.settings.isPinCodeSet) {
        viewModel.dispatch(GlobalAction.Navigate(Screen.PinCode.route, "1"))
    } else {
        viewModel.dispatch(GlobalAction.Navigate(Screen.Chat.route))
    }
}