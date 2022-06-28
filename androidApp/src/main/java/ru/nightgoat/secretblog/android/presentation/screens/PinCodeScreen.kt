package ru.nightgoat.secretblog.android.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.composables.AppAlert
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.GlobalAction
import ru.nightgoat.secretblog.core.action.PinCodeAction
import ru.nightgoat.secretblog.providers.strings.Dictionary
import ru.nightgoat.secretblog.providers.strings.EnglishDictionary
import ru.nightgoat.secretblog.utils.GlobalConstants

private const val pincodeMaxLength = 4
private const val pincodeDotRadius = 16f
private const val pincodeDotRadiusFilledSpringMin = 20f
private const val pincodeDotRadiusFilledSpringMax = 26f
private const val pinButtonBorderRadius = 2
private const val pinButtonContainerSize = 64

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
    MainContent(
        state = state,
        dictionary = dictionary,
        pincode = enteredPincode,
        pincodeScreenState = pincodeScreenState,
        onButtonClick = { buttonText ->
            val newPincode = enteredPincode.plus(buttonText)
            enteredPincode = newPincode
            val isPincodeMax = newPincode.length >= pincodeMaxLength
            when {
                isPincodeMax && pincodeScreenState == Screen.PinCode.State.SET -> {
                    viewModel.dispatch(PinCodeAction.SetPincode(enteredPincode))
                }
                isPincodeMax && pincodeScreenState != Screen.PinCode.State.SET -> {
                    viewModel.dispatch(PinCodeAction.CheckPincode(enteredPincode))
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
private fun MainContent(
    state: AppState = AppState(),
    dictionary: Dictionary,
    pincodeScreenState: Screen.PinCode.State = Screen.PinCode.State.CHECK_ON_VISIBILITY,
    pincode: String = "",
    onButtonClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onBackClick: (() -> Unit) = {},
    onCantRememberClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
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
            .padding(bottom = 64.dp, top = 4.dp)
            .clickable(onClick = onClick),
        text = dictionary.cannotRememberPin,
        color = AppColor.appleBlue,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Numpad(
    dictionary: Dictionary,
    pincodeScreenState: Screen.PinCode.State,
    onButtonClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    PincodeRow("1", "2", "3", onButtonClick = onButtonClick)
    PincodeRow("4", "5", "6", onButtonClick = onButtonClick)
    PincodeRow("7", "8", "9", onButtonClick = onButtonClick)
    val lastRowFirstText = "↩".takeIf { pincodeScreenState.isBackButtonVisible() }.orEmpty()
    PincodeRow(
        firstText = lastRowFirstText,
        secondText = "0",
        thirdText = "⌫",
        onButtonClick = onButtonClick,
        onDeleteClick = onDeleteClick,
        onBackClick = onBackClick,
    )
}

@Composable
private fun Dots(pincode: String) {
    val pincodeLength = pincode.length
    val animateFloat = remember { Animatable(0f) }
    val filledPincodeDots = pincodeMaxLength - pincodeLength
    LaunchedEffect(pincodeLength) {
        animateFloat.animateTo(
            targetValue = (pincodeDotRadiusFilledSpringMax - pincodeDotRadius),
            animationSpec = tween(GlobalConstants.PIN_CODE_TWEEN_TIME / 2)
        )
        animateFloat.animateTo(
            targetValue = (pincodeDotRadiusFilledSpringMin - pincodeDotRadius),
            animationSpec = tween(GlobalConstants.PIN_CODE_TWEEN_TIME / 2)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier) {
            for (i in 0 until pincodeMaxLength) {
                val offset = Offset(x = getDotXOffset(i), y = 0f)
                val isDotFilled = filledPincodeDots <= i
                var radius = pincodeDotRadius
                var color = Color.Gray
                val isLastFilledPin = i == filledPincodeDots
                when {
                    isDotFilled && isLastFilledPin -> {
                        radius += animateFloat.value
                        color = AppColor.appleBlue
                    }
                    isDotFilled -> {
                        radius = pincodeDotRadiusFilledSpringMin
                        color = AppColor.appleBlue
                    }
                }
                drawCircle(
                    color = color,
                    radius = radius,
                    center = offset
                )
            }
        }
    }
}

private fun getDotXOffset(dotIndex: Int) =
    (pincodeDotRadius * 6) - (dotIndex * (pincodeDotRadius * 4))

@Composable
private fun PincodeRow(
    firstText: String = "",
    secondText: String = "",
    thirdText: String = "",
    onButtonClick: (String) -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onDeleteClick == null || onBackClick == null) {
            PincodeButton(text = firstText, onClick = onButtonClick)
            PincodeButton(text = secondText, onClick = onButtonClick)
            PincodeButton(text = thirdText, onClick = onButtonClick)
        } else {
            PincodeButton(text = firstText, onBackClick = onBackClick)
            PincodeButton(text = secondText, onClick = onButtonClick)
            PincodeButton(text = thirdText, onDeleteClick = onDeleteClick)
        }
    }
}

@Composable
private fun PincodeButton(
    text: String = "",
    onClick: (String) -> Unit = {},
    onDeleteClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null
) {
    if (text.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(defaultPadding)
                .size(pinButtonContainerSize.dp)
                .clip(CircleShape)                       // clip to the circle shape
                .border(pinButtonBorderRadius.dp, Color.Gray, CircleShape)
                .clickable {
                    if (text.isNotEmpty() && onDeleteClick == null) {
                        onClick(text)
                    }
                    onDeleteClick?.let {
                        it()
                    }
                    onBackClick?.let {
                        it()
                    }
                },
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Box(
            modifier = Modifier
                .padding(defaultPadding)
                .size(pinButtonContainerSize.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PincodePreview() {
    MainContent(
        state = AppState(),
        dictionary = EnglishDictionary,
        pincode = "1"
    )
}