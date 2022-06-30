package ru.nightgoat.secretblog.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.nightgoat.kexcore.utils.logging.ILogger

fun debugBuild() {
    Napier.base(DebugAntilog())
}

object NapierKexLogger : ILogger {
    override fun loggD(message: String, tag: String?) {
        Napier.d(message = message, tag = tag)
    }

    override fun loggE(message: String, tag: String?, e: Throwable?) {
        Napier.e(message = message, tag = tag, throwable = e)
    }
}