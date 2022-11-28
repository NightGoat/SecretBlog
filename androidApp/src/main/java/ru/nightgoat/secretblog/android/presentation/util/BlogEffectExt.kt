package ru.nightgoat.secretblog.android.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ru.nightgoat.secretblog.core.BlogEffect


@Composable
fun BlogEffect.launch(fund: () -> Unit) {
    LaunchedEffect(this) {
        fund()
    }
}