package ru.nightgoat.secretblog.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import ru.nightgoat.secretblog.data.Entity
import ru.nightgoat.secretblog.utils.TimeUtils
import ru.nightgoat.secretblog.utils.getFullTimeStamp

class BlogMessage : RealmObject, Entity {

    var id: ObjectId = ObjectId.create()
    var text: String = ""
    var time: RealmInstant = TimeUtils.nowRealmInstant
    var isSecret: Boolean = false
    var isSelected: Boolean = false

    fun copy(
        id: ObjectId = this.id,
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
}