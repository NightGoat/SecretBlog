package ru.nightgoat.secretblog.android.presentation.screens.pincode

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import ru.nightgoat.secretblog.android.presentation.AppColor
import ru.nightgoat.secretblog.utils.GlobalConstants

private const val PIN_DOT_RADIUS = 16f
private const val PIN_DOT_RADIUS_FILLED_SPRING_MIN = 20f
private const val PIN_DOT_RADIUS_FILLED_SPRING_MAX = 26f

@Composable
fun Dots(pincode: String) {
    val pincodeLength = pincode.length
    val animateFloat = remember { Animatable(0f) }
    val filledPincodeDots = PIN_MAX_LENGTH - pincodeLength
    LaunchedEffect(pincodeLength) {
        animateFloat.animateTo(
            targetValue = (PIN_DOT_RADIUS_FILLED_SPRING_MAX - PIN_DOT_RADIUS),
            animationSpec = tween(GlobalConstants.PIN_CODE_TWEEN_TIME / 2)
        )
        animateFloat.animateTo(
            targetValue = (PIN_DOT_RADIUS_FILLED_SPRING_MIN - PIN_DOT_RADIUS),
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
            for (i in 0 until PIN_MAX_LENGTH) {
                val offset = Offset(x = getDotXOffset(i), y = 0f)
                val isDotFilled = filledPincodeDots <= i
                var radius = PIN_DOT_RADIUS
                var color = AppColor.elephantBone
                val isLastFilledPin = i == filledPincodeDots
                when {
                    isDotFilled && isLastFilledPin -> {
                        radius += animateFloat.value
                        color = AppColor.beige
                    }
                    isDotFilled -> {
                        radius = PIN_DOT_RADIUS_FILLED_SPRING_MIN
                        color = AppColor.beige
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
    (PIN_DOT_RADIUS * 6) - (dotIndex * (PIN_DOT_RADIUS * 4))
