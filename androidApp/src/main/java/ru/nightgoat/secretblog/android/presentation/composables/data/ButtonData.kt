package ru.nightgoat.secretblog.android.presentation.composables.data

import androidx.compose.ui.graphics.Color
import ru.nightgoat.secretblog.android.presentation.AppColor

data class ButtonData(
    val text: String,
    val color: Color = AppColor.blue,
    val onClick: () -> Unit = {},
)