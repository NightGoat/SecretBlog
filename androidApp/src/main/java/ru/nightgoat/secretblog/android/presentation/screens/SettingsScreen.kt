package ru.nightgoat.secretblog.android.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
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
        onBackPressed = {
            viewModel.reduceSideEffect(BlogEffect.NavigateBack)
        },
        onPincodeCheck = { isChecked ->
            if (isChecked) {
                viewModel.dispatch(
                    GlobalAction.Navigate(
                        Screen.PinCode.route,
                        Screen.PinCode.IS_PINCODE_SET
                    )
                )
            } else {
                viewModel.dispatch(SettingsAction.TurnOffPincode)
            }
        }
    )
}

@Composable
private fun MainContent(
    state: AppState = AppState(),
    onBackPressed: () -> Unit = {},
    onPincodeCheck: (Boolean) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Toolbar("Settings") {
            onBackPressed()
        }
        Settings(state, onPincodeCheck)
    }
}

@Composable
private fun Settings(
    state: AppState,
    onPincodeCheck: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(defaultPadding)
    ) {
        SettingsCheckBox("Pin code", state, onPincodeCheck)
    }
}

@Composable
private fun SettingsCheckBox(
    text: String,
    state: AppState,
    onCheckBoxClick: (Boolean) -> Unit
) {
    val isPinCodeSet = state.settings.isPinCodeSet
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckBoxClick(!isPinCodeSet)
            }
    ) {
        Text(
            modifier = Modifier.padding(start = defaultPadding),
            text = text
        )
        Checkbox(
            checked = isPinCodeSet,
            onCheckedChange = onCheckBoxClick
        )
    }
}

@Composable
fun Toolbar(
    title: String = "",
    isBackButtonVisible: Boolean = true,
    onBackPressed: () -> Unit = {},
) {
    Surface(
        elevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isBackButtonVisible) {
                SimpleSpacer()
                Image(
                    modifier = Modifier
                        .clickable {
                            onBackPressed()
                        }
                        .padding(defaultPadding),
                    painter = painterResource(id = R.drawable.ic_outline_arrow_back_24),
                    contentDescription = "go back"
                )
            }
            if (title.isNotEmpty()) {
                SimpleSpacer()
                Text(
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    text = title
                )
            }
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