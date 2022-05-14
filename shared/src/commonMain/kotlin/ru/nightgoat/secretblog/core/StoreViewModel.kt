package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.kodein.memory.util.UUID
import org.koin.core.component.KoinComponent
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.models.BlogMessage

class StoreViewModel(
    private val dataBase: DataBase<BlogMessage>
) : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.Main),
    Store<AppState, BlogAction, BlogEffect> {

    private val state = MutableStateFlow(AppState())
    private val sideEffect = MutableSharedFlow<BlogEffect>()
    private val messages = dataBase.flow.stateIn(this, SharingStarted.Eagerly, emptySequence())

    fun addMessage(message: String, isSecret: Boolean = false) {
        message.takeIf { it.isNotEmpty() }?.let { newMessage ->
            val action = BlogAction.AddMessage(
                message = newMessage,
                isSecret = isSecret
            )

            dispatch(action)
        }
    }

    override fun observeState(): StateFlow<AppState> = state

    override fun observeSideEffect(): Flow<BlogEffect> = sideEffect

    override fun dispatch(action: BlogAction) {
        val oldState = state.value
        var newState = oldState
        when (action) {
            is BlogAction.Start -> {
                launch {
                    state.value = oldState.copy(blogMessages = dataBase.getAll().toList())
                }
            }
            is BlogAction.Refresh -> {
                newState = oldState.stateWithNewMessages()
            }
            is BlogAction.AddMessage -> {
                launch {
                    val newMessage = BlogMessage(
                        text = action.message,
                        isSecret = action.isSecret
                    )
                    addMessage(newMessage)
                }
            }
            is BlogAction.RemoveMessage -> {
                launch {
                    deleteMessage(action.message.id)
                }
            }
            is BlogAction.ReverseSecretBlogsVisibility -> {
                newState = oldState.reversedVisibilty
            }
        }
        state.value = newState
    }

    private suspend fun deleteMessage(messageId: UUID) {
        dataBase.delete(messageId)
        dispatch(BlogAction.Refresh)
    }

    private suspend fun addMessage(message: BlogMessage) {
        dataBase.add(message)
        dispatch(BlogAction.Refresh)
    }

    private fun AppState.stateWithNewMessages() = this.copy(blogMessages = messages.value.toList())
}