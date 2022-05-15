package ru.nightgoat.secretblog.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.util.UUID
import ru.nightgoat.secretblog.data.Entity

@Serializable
data class BlogMessage(
    override val id: UUID = UUID.randomUUID(),
    val text: String = "",
    val time: Instant = Clock.System.now(),
    val isSecret: Boolean = false,
    val isSelected: Boolean = false,
) : Metadata, Entity {

    val timeFormatted = time.toLocalDateTime(TimeZone.currentSystemDefault()).run {
        "${dayOfMonth.addZero()}.${monthNumber.addZero()}.${year.addZero()} ${hour.addZero()}:${minute.addZero()}:${second.addZero()}"
    }

    private fun Int.addZero() = this.toString().padStart(2, '0')
    fun makeSecret() = this.copy(isSecret = true)
    fun revealSecret() = this.copy(isSecret = false)
}