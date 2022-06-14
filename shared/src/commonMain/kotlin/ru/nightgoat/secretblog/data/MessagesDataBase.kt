package ru.nightgoat.secretblog.data

import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.DataBaseProvider

class MessagesDataBase(private val provider: DataBaseProvider) : DataBase<BlogMessage> {

    override val db by lazy {
        provider.db
    }

    override suspend fun add(entity: BlogMessage) {
        db.write {
            copyToRealm(entity.copy(id = ObjectId.create()))
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