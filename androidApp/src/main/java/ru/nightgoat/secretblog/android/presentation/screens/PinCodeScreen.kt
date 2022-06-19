package ru.nightgoat.secretblog.android.presentation.screens

import androidx.activity.compose.BackHandler
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

private const val pincodeMaxLength = 4
private const val pincodeDotRadius = 16f
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
    BackHandler {

    }
    val isPincodeCheck = remember {
        isPincodeCheckArg == Screen.PinCode.IS_PINCODE_CHECK
    }
    var enteredPincode by remember { mutableStateOf("") }
    when (sideEffect) {
        is BlogEffect.PincodeCheckResult -> {
            LaunchedEffect(enteredPincode) {
                val isPincodeCorrect = sideEffect.isPincodeRight
                if (isPincodeCorrect) {
                    viewModel.dispatch(GlobalAction.Navigate(Screen.Chat.route))
                }
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
        else -> Unit
    }
    MainContent(
        state = state,
        dictionary = dictionary,
        pincode = enteredPincode,
        isPincCodeCheckScreen = isPincodeCheck,
        onButtonClick = { buttonText ->
            val newPincode = enteredPincode.plus(buttonText)
            enteredPincode = newPincode
            val isPincodeMax = newPincode.length >= 4
            when {
                isPincodeMax && !isPincodeCheck -> {
                    viewModel.dispatch(PinCodeAction.SetPincode(enteredPincode))
                    enteredPincode = ""
                }
                isPincodeMax && isPincodeCheck -> {
                    viewModel.dispatch(PinCodeAction.CheckPincode(enteredPincode))
                    enteredPincode = ""
                }
            }
        },
        onDeleteClick = {
            val newPincode = enteredPincode.dropLast(1)
            enteredPincode = newPincode
        },
        onCantRememberClick = {
            viewModel.dispatch(PinCodeAction.CannotRememberPinCode)
        }
    )
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
    isPincCodeCheckScreen: Boolean = true,
    pincode: String = "",
    onButtonClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
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
            Numpad(onButtonClick, onDeleteClick)
        }
        if (isPincCodeCheckScreen) {
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
    onButtonClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    PincodeRow("1", "2", "3", onButtonClick = onButtonClick)
    PincodeRow("4", "5", "6", onButtonClick = onButtonClick)
    PincodeRow("7", "8", "9", onButtonClick = onButtonClick)
    PincodeRow(
        secondText = "0",
        thirdText = "⌫",
        onButtonClick = onButtonClick,
        onDeleteClick = onDeleteClick
    )
}

@Composable
private fun Dots(pincode: String) {
    val pincodeLength = pincode.length
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Canvas(modifier = Modifier) {
            for (i in 0 until pincodeMaxLength) {
                val color = if (pincodeMaxLength - pincodeLength <= i) {
                    AppColor.appleBlue
                } else {
                    Color.Gray
                }
                drawCircle(
                    color = color,
                    radius = pincodeDotRadius,
                    center = Offset(x = 96 - (i * 64f), y = 0f)
                )
            }
        }
    }
}


@Composable
private fun PincodeRow(
    firstText: String = "",
    secondText: String = "",
    thirdText: String = "",
    onButtonClick: (String) -> Unit = { _ -> },
    onDeleteClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PincodeButton(text = firstText, onClick = onButtonClick)
        PincodeButton(text = secondText, onClick = onButtonClick)
        if (onDeleteClick != null) {
            PincodeButton(text = thirdText, onDeleteClick = onDeleteClick)
        } else {
            PincodeButton(text = thirdText, onClick = onButtonClick)
        }
    }
}

@Composable
private fun PincodeButton(
    text: String = "",
    onClick: (String) -> Unit = {},
    onDeleteClick: (() -> Unit)? = null
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