package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.SettingsAction

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    sideEffect: BlogEffect
) {
    MainContent(
        state = state,
        onPincodeCheck = { isChecked ->
            if (isChecked) {
                viewModel.dispatch(GlobalAction.Navigate(Screen.PinCode.route, "0"))
            } else {
                viewModel.dispatch(SettingsAction.TurnOffPincode)
            }
        }
    )
}

@Composable
private fun MainContent(
    state: AppState = AppState(),
    onPincodeCheck: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pin code")
            Checkbox(
                checked = state.settings.isPinCodeSet,
                onCheckedChange = onPincodeCheck
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SettingsPreview() {
    MainContent()
}