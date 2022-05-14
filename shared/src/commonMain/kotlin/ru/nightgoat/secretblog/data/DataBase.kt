package ru.nightgoat.secretblog.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.db.*
import org.kodein.db.impl.open
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.io.ReadMemory
import org.kodein.memory.util.UUID
import ru.nightgoat.secretblog.models.BlogMessage
import kotlin.properties.Delegates

object MessagesDataBase : DataBase<BlogMessage>, DBListener<BlogMessage> {

    private var db: DB by Delegates.notNull()

    override val flow = MutableStateFlow(emptySequence<BlogMessage>())

    override suspend fun init(path: String) {
        db = DB.open(path)
        db.on<BlogMessage>().register(this)
        flow.value = getAll()
    }

    override suspend fun add(message: BlogMessage) {
        db.put(message)
    }

    override suspend fun delete(id: UUID) {
        db.deleteById<BlogMessage>(id)
    }

    override suspend fun getAll(): Sequence<BlogMessage> {
        return db.find(BlogMessage::class).all().asModelSequence()
    }


    override fun didPut(
        model: BlogMessage,
        key: Key<BlogMessage>,
        typeName: ReadMemory,
        metadata: Metadata,
        size: Int,
        options: Array<out Options.Puts>
    ) {
        super.didPut(model, key, typeName, metadata, size, options)
        flow.value += model
    }

    override fun didDelete(
        key: Key<BlogMessage>,
        model: BlogMessage?,
        typeName: ReadMemory,
        options: Array<out Options.Deletes>
    ) {
        super.didDelete(key, model, typeName, options)
        model?.let {
            flow.value -= model
        }
    }


}

interface DataBase<T : Entity> {
    val flow: Flow<Sequence<T>>
    suspend fun init(path: String)
    suspend fun add(message: T)
    suspend fun delete(id: UUID)
    suspend fun getAll(): Sequence<T>
}