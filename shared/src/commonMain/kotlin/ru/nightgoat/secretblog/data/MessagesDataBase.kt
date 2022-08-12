package ru.nightgoat.secretblog.data

import io.github.aakira.napier.Napier
import io.github.nightgoat.kexcore.orZero
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.internal.platform.ensureNeverFrozen
import io.realm.kotlin.internal.platform.isFrozen
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.DataBaseProvider
import ru.nightgoat.secretblog.utils.Tagable
import ru.nightgoat.secretblog.utils.Try
import ru.nightgoat.secretblog.utils.logFailure
import ru.nightgoat.secretblog.utils.logFailureNoStackTrace

class MessagesDataBase(private val provider: DataBaseProvider) : DataBase<BlogMessage>, Tagable {

    var lastId: Long = 0

    override val db by lazy {
        provider.db
    }

    override suspend fun init(): List<BlogMessage> {
        return Try {
            val all = getAll()
            lastId = all.lastOrNull()?.id.orZero()
            all
        }.logFailureNoStackTrace {
            "init failure"
        }.getOrDefault {
            emptyList()
        }
    }

    override suspend fun add(entity: BlogMessage): Long {
        Try {
            val newId = lastId + 1
            Napier.d { "Added message: $entity" }
            lastId = newId
            db.write {
                copyToRealm(entity.copy(id = lastId))
            }
        }.logFailureNoStackTrace {
            "add entity failure"
        }

        return lastId
    }

    override suspend fun delete(entity: BlogMessage) {
        Try {
            db.write {
                val message = query<BlogMessage>("id == ${entity.id}").first().find()
                message?.also {
                    delete(it)
                }
            }
        }.logFailureNoStackTrace {
            "delete entity failure"
        }
    }

    override suspend fun getAll(): List<BlogMessage> {
        return Try {
            db.query<BlogMessage>().find() as List<BlogMessage>
        }.logFailureNoStackTrace {
            "getAll failure"
        }.getOrDefault {
            emptyList()
        }
    }

    override suspend fun deleteAll() {
        Try {
            lastId = 0
            db.write {
                val messages = query<BlogMessage>().find()
                delete(messages)
            }
        }.logFailureNoStackTrace {
            "deleteAll failure"
        }
    }

    override suspend fun update(entity: BlogMessage) {
        Try {
            db.write {
                copyToRealm(entity, UpdatePolicy.ALL)
            }
        }.logFailureNoStackTrace {
            "update failure"
        }
    }
}