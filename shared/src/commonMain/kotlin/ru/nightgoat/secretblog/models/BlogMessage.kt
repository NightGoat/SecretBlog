package ru.nightgoat.secretblog.models

import kotlinx.serialization.Serializable
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.util.UUID
import ru.nightgoat.secretblog.data.Entity

@Serializable
data class BlogMessage(
    override val id: UUID = UUID.randomUUID(),
    val text: String = "",
    val isSecret: Boolean = false
) : Metadata, Entity {
    fun makeSecret() = this.copy(isSecret = true)
    fun revealSecret() = this.copy(isSecret = false)
}