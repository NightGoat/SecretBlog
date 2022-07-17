package ru.nightgoat.secretblog.data

import io.github.aakira.napier.Napier
import io.realm.kotlin.ext.query
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.DataBaseProvider

class MessagesDataBase(private val provider: DataBaseProvider) : DataBase<BlogMessage> {

    var lastId: Long = 0

    override val db by lazy {
        provider.db
    }

    override suspend fun init(): List<BlogMessage> {
        val all = getAll()
        lastId = all.lastOrNull()?.id ?: 0
        return all
    }

    override suspend fun add(entity: BlogMessage): Long {
        val newId = lastId + 1
        lastId = newId
        db.write {
            copyToRealm(entity.copy(id = lastId))
        }
        return lastId
    }

    override suspend fun delete(entity: BlogMessage) {
        kotlin.runCatching {
            db.write {
                val message = query<BlogMessage>("id == ${entity.id}").first().find()
                message?.also {
                    delete(it)
                }
            }
        }.onFailure {
            Napier.e(it, TAG) {
                "delete entity failue"
            }
        }
    }

    override suspend fun getAll(): List<BlogMessage> {
        return db.query<BlogMessage>().find()
    }

    override suspend fun deleteAll() {
        lastId = 0
        db.write {
            val messages = query<BlogMessage>().find()
            delete(messages)
        }
    }

    override suspend fun update(entity: BlogMessage) {
        db.write {
            val message = query<BlogMessage>("id == ${entity.id}").first().find()
            message?.also {
                delete(it)
            }
            copyToRealm(entity)
        }
    }

    companion object {
        private const val TAG = "MessagesDataBase"
    }
}