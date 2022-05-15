package ru.nightgoat.secretblog.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.kodein.db.DB
import org.kodein.db.asModelSequence
import org.kodein.db.deleteAll
import org.kodein.db.deleteById
import org.kodein.db.impl.open
import ru.nightgoat.secretblog.models.BlogMessage
import kotlin.properties.Delegates

object MessagesDataBase : DataBase<BlogMessage> {

    private var db: DB by Delegates.notNull()

    override val flow = MutableStateFlow(emptyList<BlogMessage>())

    override suspend fun init(path: String) {
        db = DB.open(path)
        flow.value = getAll().toList()
    }

    override suspend fun add(entity: BlogMessage) {
        withContext(Dispatchers.Default) {
            val oldFlow = flow.value
            flow.value = oldFlow + entity
            db.put(entity)
        }
    }

    override suspend fun delete(entity: BlogMessage) {
        withContext(Dispatchers.Default) {
            val oldFlow = flow.value
            flow.value = oldFlow - entity
            db.deleteById<BlogMessage>(entity.id)
        }
    }

    override suspend fun getAll(): List<BlogMessage> {
        return withContext(Dispatchers.Default) {
            db.find(BlogMessage::class).all().asModelSequence().sortedByDescending {
                it.time
            }.toList()
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.Default) {
            db.deleteAll(db.find(BlogMessage::class).all())
            flow.value = emptyList()
        }
    }
}

interface DataBase<T : Entity> {
    val flow: Flow<List<T>>
    suspend fun init(path: String)
    suspend fun add(entity: T)
    suspend fun delete(entity: T)
    suspend fun getAll(): List<T>
    suspend fun deleteAll()
}