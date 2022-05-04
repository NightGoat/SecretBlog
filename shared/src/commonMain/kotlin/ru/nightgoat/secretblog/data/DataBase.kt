package ru.nightgoat.secretblog.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.kodein.db.*
import org.kodein.db.impl.open
import org.kodein.db.model.orm.Metadata
import org.kodein.memory.io.ReadMemory
import org.kodein.memory.util.UUID
import ru.nightgoat.secretblog.models.BlogMessage
import kotlin.properties.Delegates

object MessagesDataBase : DataBase<BlogMessage>, DBListener<BlogMessage> {

    private const val DB_PATH = "db"

    private var db: DB by Delegates.notNull()

    private val stateFlow = MutableStateFlow(0)

    override val flow = flow {
        stateFlow.map {
            emit(getAll())
        }
    }

    fun init(path: String) {
        db = DB.open(path)
        db.on<BlogMessage>().register(this)
    }

    override fun add(message: BlogMessage) {
        db.put(message)
    }

    override fun delete(id: UUID) {
        db.deleteById<BlogMessage>(id)
    }

    override fun getAll(): Sequence<BlogMessage> {
        return db.find<BlogMessage>().all().asModelSequence()
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
        stateFlow.value += 1
    }

    override fun didDelete(
        key: Key<BlogMessage>,
        model: BlogMessage?,
        typeName: ReadMemory,
        options: Array<out Options.Deletes>
    ) {
        super.didDelete(key, model, typeName, options)
        stateFlow.value += 1
    }


}

interface DataBase<T : Entity> {
    val flow: Flow<Sequence<T>>
    fun add(message: T)
    fun delete(id: UUID)
    fun getAll(): Sequence<T>
}