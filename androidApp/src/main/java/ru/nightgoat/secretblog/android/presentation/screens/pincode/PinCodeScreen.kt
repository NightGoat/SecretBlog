package ru.nightgoat.secretblog.android.presentation.screens.pincode

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.BlogTheme
import ru.nightgoat.secretblog.android.presentation.composables.AppAlert
import ru.nightgoat.secretblog.android.presentation.composables.SimpleSpacer
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.PinCodeAction
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary

const val PIN_MAX_LENGTH = 4
private val cannotRememberPinButtonTextSize = 18.sp

@Composable
fun PinCodeScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    sideEffect: BlogEffect,
    isPincodeCheckArg: String,
    dictionary: Dictionary
) {
    val pincodeScreenState = Screen.PinCode.State.fromArg(isPincodeCheckArg)
    val onBackClick = { navController.popBackStack() }
    BackHandler {
        if (pincodeScreenState.isBackButtonVisible()) {
            onBackClick()
        }
    }
    var enteredPincode by remember { mutableStateOf("") }
    when (sideEffect) {
        is BlogEffect.PincodeCheckResult -> {
            LaunchedEffect(enteredPincode) {
                reducePincodeCheck(sideEffect, pincodeScreenState, viewModel)
            }
        }
        is BlogEffect.CannotRememberPinCodeDialog -> {
            DeleteDatabaseDialog(
                dictionary = dictionary,
                onCancelClick = {
                    viewModel.clearSideEffects()
                },
                onYesClick = {
                    viewModel.dispatch(GlobalAction.ClearApp)
                }
            )
        }
        is BlogEffect.DropEnteredPincodeToEmpty -> {
            enteredPincode = ""
        }
        else -> Unit
    }
    PinMainContent(
        state = state,
        dictionary = dictionary,
        pincode = enteredPincode,
        pincodeScreenState = pincodeScreenState,
        onButtonClick = { buttonText ->
            if (enteredPincode.length < PIN_MAX_LENGTH) {
                val newPincode = enteredPincode.plus(buttonText)
                enteredPincode = newPincode
                val isPincodeMax = newPincode.length == PIN_MAX_LENGTH
                when {
                    isPincodeMax && pincodeScreenState == Screen.PinCode.State.SET -> {
                        viewModel.dispatch(PinCodeAction.SetPincode(enteredPincode))
                    }
                    isPincodeMax && pincodeScreenState != Screen.PinCode.State.SET -> {
                        viewModel.dispatch(PinCodeAction.CheckPincode(enteredPincode))
                    }
                }
            }
        },
        onDeleteClick = {
            val newPincode = enteredPincode.dropLast(1)
            enteredPincode = newPincode
        },
        onCantRememberClick = {
            viewModel.dispatch(PinCodeAction.CannotRememberPinCode)
        },
        onBackClick = {
            onBackClick()
        }
    )
}

private fun reducePincodeCheck(
    sideEffect: BlogEffect.PincodeCheckResult,
    pincodeScreenState: Screen.PinCode.State,
    viewModel: StoreViewModel
) {
    if (sideEffect.isPincodeRight) {
        when (pincodeScreenState) {
            Screen.PinCode.State.CHECK_ON_LOGIN -> {
                viewModel.dispatch(GlobalAction.Navigate(Screen.Chat.route))
            }
            Screen.PinCode.State.CHECK_ON_VISIBILITY -> {
                viewModel.dispatch(PinCodeAction.ReverseSecretMessagesVisibility)
            }
            Screen.PinCode.State.CHECK_ON_SETTINGS -> {

            }
            else -> Unit
        }
    }
}

@Composable
private fun DeleteDatabaseDialog(
    dictionary: Dictionary,
    onCancelClick: () -> Unit,
    onYesClick: () -> Unit
) {
    AppAlert(
        title = dictionary.eraseAppDataAlertTitle,
        message = dictionary.eraseAppDataAlertMessage,
        leftButtonText = dictionary.no,
        rightButtonText = dictionary.yes,
        onLeftButtonClick = onCancelClick,
        onRightButtonClick = onYesClick
    )
}

@Composable
private fun PinMainContent(
    state: AppState = AppState(),
    dictionary: Dictionary,
    pincodeScreenState: Screen.PinCode.State = Screen.PinCode.State.CHECK_ON_VISIBILITY,
    pincode: String = "",
    onButtonClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onBackClick: (() -> Unit) = {},
    onCantRememberClick: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colors.primaryVariant) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                Dots(pincode)
                Numpad(
                    dictionary = dictionary,
                    pincodeScreenState = pincodeScreenState,
                    onButtonClick = onButtonClick,
                    onDeleteClick = onDeleteClick,
                    onBackClick = onBackClick
                )
            }
            if (pincodeScreenState == Screen.PinCode.State.CHECK_ON_LOGIN) {
                CantRememberPincodeMessage(dictionary, onCantRememberClick)
            } else {
                SimpleSpacer(128)
            }
        }
    }
}

@Composable
fun CantRememberPincodeMessage(
    dictionary: Dictionary,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp, top = 64.dp)
            .clickable(onClick = onClick),
        text = dictionary.cannotRememberPin,
        color = AppColor.elephantBone,
        fontSize = cannotRememberPinButtonTextSize,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}




@Preview(showBackground = true)
@Composable
private fun PincodePreview() {
    BlogTheme {
        PinMainContent(
            state = AppState(),
            dictionary = EnglishDictionary,
            pincode = "1",
            pincodeScreenState = Screen.PinCode.State.CHECK_ON_LOGIN
        )
    }
}