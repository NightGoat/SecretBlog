package ru.nightgoat.secretblog.data

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ContextProvider : KoinComponent {
    private val context: Context by inject()
    fun requireContext() = context
}