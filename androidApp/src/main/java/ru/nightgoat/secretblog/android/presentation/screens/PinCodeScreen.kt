package ru.nightgoat.secretblog.android.presentation.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.android.presentation.navigate
import ru.nightgoat.secretblog.android.presentation.screens.base.Screen
import ru.nightgoat.secretblog.core.AppState
import ru.nightgoat.secretblog.core.BlogEffect
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.core.action.PinCodeAction

@Composable
fun PinCodeScreen(
    navController: NavController,
    viewModel: StoreViewModel,
    state: AppState,
    sideEffect: BlogEffect,
    isFromSplashArg: String
) {
    val isFromSplash = isFromSplashArg == "1"
    var enteredPincode by remember { mutableStateOf("") }
    when (sideEffect) {
        is BlogEffect.PincodeCheckResult -> {
            LaunchedEffect(enteredPincode) {
                val isPincodeCorrect = sideEffect.isPincodeRight
                if (isPincodeCorrect) {
                    navController.navigate(Screen.Chat)
                }
            }
        }
    }
    MainContent(
        state = state,
        pincode = enteredPincode,
        onButtonClick = { buttonText ->
            val newPincode = enteredPincode.plus(buttonText)
            enteredPincode = newPincode
            val isPincodeMax = newPincode.length >= 4
            when {
                isPincodeMax && !isFromSplash -> {
                    viewModel.dispatch(PinCodeAction.SetPincode(enteredPincode))
                    enteredPincode = ""
                }
                isPincodeMax && isFromSplash -> {
                    viewModel.dispatch(PinCodeAction.CheckPincode(enteredPincode))
                    enteredPincode = ""
                }
            }
        },
        onDeleteClick = {
            val newPincode = enteredPincode.dropLast(1)
            enteredPincode = newPincode
        }
    )
}

@Composable
private fun MainContent(
    state: AppState = AppState(),
    pincode: String = "",
    onButtonClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Dots(pincode)
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
            for (i in 0..3) {
                val color = if (4 - pincodeLength <= i) {
                    Color.Blue
                } else {
                    Color.Gray
                }
                drawCircle(
                    color = color,
                    radius = 16f,
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
                .size(64.dp)
                .clip(CircleShape)                       // clip to the circle shape
                .border(2.dp, Color.Gray, CircleShape)
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
        Box(modifier = Modifier.size(64.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PincodePreview() {
    MainContent(
        AppState(),
        pincode = "1"
    )
}