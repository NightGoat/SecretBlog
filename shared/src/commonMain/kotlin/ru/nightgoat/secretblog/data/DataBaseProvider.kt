package ru.nightgoat.secretblog.data

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import ru.nightgoat.secretblog.models.BlogMessage

class DataBaseProvider {
    private val config by lazy {
        RealmConfiguration.Builder(schema = setOf(BlogMessage::class)).build()
    }

    val db: Realm by lazy {
        Realm.open(config)
    }
}