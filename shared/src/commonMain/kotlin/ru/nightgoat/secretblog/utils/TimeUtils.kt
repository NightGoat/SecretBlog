package ru.nightgoat.secretblog.utils

import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object TimeUtils {
    const val MILLISECONDS_IN_SECOND = 1000

    val nowRealmInstant = Clock.System.now().run {
        RealmInstant.from(epochSeconds, nanosecondsOfSecond)
    }

    fun convertSecondsToMilliseconds(seconds: Long) = seconds * MILLISECONDS_IN_SECOND
}

fun RealmInstant.toKotlinInstant(): Instant {
    val millis = TimeUtils.convertSecondsToMilliseconds(this.epochSeconds)
    return Instant.fromEpochMilliseconds(millis)
}

@Deprecated("Shows timestamp in single format, its better to use native time formatting")
fun RealmInstant.getFullTimeStamp(): String {
    return this.toKotlinInstant().getFullTimeStamp()
}

@Deprecated("Shows timestamp in single format, its better to use native time formatting")
fun Instant.getFullTimeStamp(): String {
    return this.toLocalDateTime(TimeZone.currentSystemDefault()).run {
        "${dayOfMonth.addLeadingZero()}.${monthNumber.addLeadingZero()}.${year.addLeadingZero()} ${hour.addLeadingZero()}:${minute.addLeadingZero()}"
    }
}
