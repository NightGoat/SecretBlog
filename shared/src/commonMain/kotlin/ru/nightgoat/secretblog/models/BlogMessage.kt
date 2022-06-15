package ru.nightgoat.secretblog.models

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import ru.nightgoat.secretblog.data.Entity
import ru.nightgoat.secretblog.utils.TimeUtils
import ru.nightgoat.secretblog.utils.getFullTimeStamp

class BlogMessage : RealmObject, Entity {

    var id: Long = 0
    var text: String = ""
    var time: RealmInstant = TimeUtils.nowRealmInstant
    var isSecret: Boolean = false
    var isSelected: Boolean = false

    fun copy(
        id: Long = this.id,
        text: String = this.text,
        time: RealmInstant = this.time,
        isSecret: Boolean = this.isSecret,
        isSelected: Boolean = this.isSelected,
    ) = BlogMessage().apply {
        this.id = id
        this.text = text
        this.time = time
        this.isSecret = isSecret
        this.isSelected = isSelected
    }

    fun timeFormatted() = time.getFullTimeStamp()

    fun makeSecret() = this.copy(isSecret = true)
    fun revealSecret() = this.copy(isSecret = false)

    companion object {
        fun newInstance(
            id: Long = 0,
            text: String = "",
            time: RealmInstant = TimeUtils.nowRealmInstant,
            isSecret: Boolean = false,
            isSelected: Boolean = false,
        ): BlogMessage {
            val new = BlogMessage()
            return new.copy(id, text, time, isSecret, isSelected)
        }
    }
}