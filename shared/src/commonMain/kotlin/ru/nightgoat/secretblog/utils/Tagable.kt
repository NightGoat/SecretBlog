package ru.nightgoat.secretblog.utils

import io.github.nightgoat.kexcore.log
import io.github.nightgoat.kexcore.utils.Try

interface Tagable {

    open val tag: String
        get() = this::class.simpleName.orEmpty()

    fun <T> Try<T>.logFailure(message: () -> String): Try<T> {
        return this.logFailure(tag, message)
    }

    fun <T> Try<T>.logFailureNoStackTrace(message: () -> String): Try<T> {
        return this.logFailureNoStackTrace(tag, message)
    }
}

inline fun <T> Tagable.Try(tryBlock: () -> T): Try<T> {
    return Try(tag, tryBlock)
}