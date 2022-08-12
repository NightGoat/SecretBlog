package ru.nightgoat.secretblog.android.presentation.screens.pincode

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.android.presentation.defaultPadding
import ru.nightgoat.secretblog.core.Screen
import ru.nightgoat.secretblog.providers.strings.Dictionary

private const val PIN_BUTTON_BORDER_RADIUS = 2
private const val PIN_BUTTON_CONTAINER_SIZE = 64
private val pinButtonTextSize = 24.sp

@Composable
fun Numpad(
    dictionary: Dictionary,
    pincodeScreenState: Screen.PinCode.State,
    onButtonClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    with(PincodeSymbols) {
        PincodeRow(ONE, TWO, THREE, onButtonClick = onButtonClick)
        PincodeRow(FOUR, FIVE, SIX, onButtonClick = onButtonClick)
        PincodeRow(SEVEN, EIGHT, NINE, onButtonClick = onButtonClick)
        val lastRowFirstText = ENTER.takeIf { pincodeScreenState.isBackButtonVisible() }.orEmpty()
        PincodeRow(
            firstText = lastRowFirstText,
            secondText = ZERO,
            thirdText = DELETE,
            onButtonClick = onButtonClick,
            onDeleteClick = onDeleteClick,
            onBackClick = onBackClick,
        )
    }
}

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
                .size(PIN_BUTTON_CONTAINER_SIZE.dp)
                .clip(CircleShape)
                .border(PIN_BUTTON_BORDER_RADIUS.dp, AppColor.beige, CircleShape)
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
                fontSize = pinButtonTextSize,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Box(
            modifier = Modifier
                .padding(defaultPadding)
                .size(PIN_BUTTON_CONTAINER_SIZE.dp)
        )
    }
}