package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    LaunchedEffect(viewModel) {
        viewModel.dispatch(BlogAction.Start)
    }
    if (sideEffect is BlogEffect.LoadSuccess) {
        val isPincodeSet = state.settings.isPinCodeSet
        if (isPincodeSet) {
            viewModel.dispatch(
                GlobalAction.Navigate(
                    Screen.PinCode.route,
                    Screen.PinCode.IS_PINCODE_CHECK
                )
            )
        } else {
            viewModel.dispatch(GlobalAction.Navigate(Screen.Chat.route))
        }
    }

}