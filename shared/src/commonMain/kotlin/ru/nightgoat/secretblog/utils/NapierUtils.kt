package ru.nightgoat.secretblog.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.nightgoat.kexcore.utils.Try
import io.github.nightgoat.kexcore.utils.logging.ILogger

fun <T> Try<T>.logFailure(tag: String? = null, message: () -> String): Try<T> {
    if (this is Try.Failure) {
        Napier.e(this.throwable, tag, message)
    }
    return this
}

fun <T> Try<T>.logFailureNoStackTrace(tag: String? = null, message: () -> String): Try<T> {
    if (this is Try.Failure) {
        Napier.e(message = message, tag = tag)
    }
    return this
}

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