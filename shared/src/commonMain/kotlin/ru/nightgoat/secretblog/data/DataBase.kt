package ru.nightgoat.secretblog.data

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nightgoat.secretblog.models.BlogMessage

class MessagesDataBase : DataBase<BlogMessage> {

    private val config by lazy {
        RealmConfiguration.Builder(schema = setOf(BlogMessage::class)).build()
    }
    private val db: Realm by lazy {
        Realm.open(config)
    }

    override val flow by lazy {
        db.query<BlogMessage>().find().asFlow().map {
            it.list
        }
    }

    suspend fun getLastId(): Int {
        return getAll().lastOrNull()?.id ?: 0
    }

    override suspend fun init() {
    }

    override suspend fun add(entity: BlogMessage) {
        val lastId = getLastId()
        db.write {
            copyToRealm(entity.copy(id = lastId + 1))
        }
    }

    override suspend fun delete(entity: BlogMessage) {
        db.write {
            val message = query<BlogMessage>("id == ${entity.id}").first().find()
            message?.also {
                delete(it)
            }
        }
    }

    override suspend fun getAll(): List<BlogMessage> {
        return db.query<BlogMessage>().find()
    }

    override suspend fun deleteAll() {
        db.write {
            val messages = query<BlogMessage>().find()
            delete(messages)
        }
    }
}

interface DataBase<T : Entity> {
    val flow: Flow<List<T>>
    suspend fun init()
    suspend fun add(entity: T)
    suspend fun delete(entity: T)
    suspend fun getAll(): List<T>
    suspend fun deleteAll()
}