package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.reduxkotlin.Store
import org.reduxkotlin.createThreadSafeStore
import ru.nightgoat.secretblog.data.MessagesDataBase
import kotlin.coroutines.CoroutineContext

class StoreViewModel : KoinComponent, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val database = MessagesDataBase
    var store: Store<AppState>? = null
    val messages by lazy {
        database.flow
    }

    init {
        val allItems = database.getAll()
        store = createThreadSafeStore(
            reducer = reducer,
            preloadedState = AppState(
                blogMessages = allItems.toList()
            )
        )
    }

    fun addMessage(message: String, isSecret: Boolean = false) {
        message.takeIf { it.isNotEmpty() }?.let { newMessage ->
            val action = BlogAction.AddMessage(
                message = newMessage,
                isSecret = isSecret
            )

            store?.dispatch?.let {
                it(action)
            }
        }
    }

}