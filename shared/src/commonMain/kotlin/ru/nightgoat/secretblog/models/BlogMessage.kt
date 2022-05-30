package ru.nightgoat.secretblog.models

import io.realm.RealmObject
import ru.nightgoat.secretblog.data.Entity

class BlogMessage : RealmObject, Entity {

    var id: Int = 0
    var text: String = ""

    //    var time: Instant = Clock.System.now()
    var isSecret: Boolean = false
    var isSelected: Boolean = false

    fun copy(
        id: Int = this.id,
        text: String = this.text,
//        time: Instant = this.time,
        isSecret: Boolean = this.isSecret,
        isSelected: Boolean = this.isSelected,
    ) = BlogMessage().apply {
        this.id = id
        this.text = text
//        this.time = time
        this.isSecret = isSecret
        this.isSelected = isSelected
    }

//    val timeFormatted = time.toLocalDateTime(TimeZone.currentSystemDefault()).run {
//        "${dayOfMonth.addZero()}.${monthNumber.addZero()}.${year.addZero()} ${hour.addZero()}:${minute.addZero()}:${second.addZero()}"
//    }

    private fun Int.addZero() = this.toString().padStart(2, '0')
    fun makeSecret() = this.copy(isSecret = true)
    fun revealSecret() = this.copy(isSecret = false)
}