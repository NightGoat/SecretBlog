package ru.nightgoat.secretblog.models

import org.kodein.memory.util.UUID
import ru.nightgoat.secretblog.data.Entity

data class BlogMessage(
    val id: UUID = UUID.randomUUID(),
    val text: String = "",
    val isSecret: Boolean = false
) : Entity {
    fun makeSecret() = this.copy(isSecret = true)
    fun revealSecret() = this.copy(isSecret = false)
}