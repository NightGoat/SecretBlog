package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.models.BlogMessage

class StoreViewModel(
    private val dataBase: DataBase<BlogMessage>
) : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.Main),
    Store<AppState, BlogAction, BlogEffect> {

    private val state = MutableStateFlow(AppState())
    private val sideEffect = MutableSharedFlow<BlogEffect>()
    private val messages = dataBase.flow.stateIn(this, SharingStarted.Eagerly, emptyList())

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
        when (action) {
            is BlogAction.Start -> {
                launch {
                    val newMessages = dataBase.getAll()
                    state.value = oldState.copy(blogMessages = newMessages)
                    sideEffect.emit(BlogEffect.ScrollToLastElement)
                }
            }
            is BlogAction.Refresh -> {
                state.value = oldState.stateWithNewMessages()
                sideEffect.tryEmit(BlogEffect.ScrollToLastElement)
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
            is BlogAction.ClearDB -> {
                launch {
                    dataBase.deleteAll()
                    refresh()
                }
            }
            is BlogAction.RemoveMessage -> {
                launch {
                    deleteMessage(action.message)
                }
            }
            is BlogAction.ReverseSecretBlogsVisibility -> {
                state.value = oldState.reversedVisibilty
            }
        }
    }

    private suspend fun deleteMessage(message: BlogMessage) {
        dataBase.delete(message)
        refresh()
    }

    private suspend fun addMessage(message: BlogMessage) {
        dataBase.add(message)
        refresh()
    }

    fun clearDB() {
        dispatch(BlogAction.ClearDB)
    }

    private fun refresh() {
        dispatch(BlogAction.Refresh)
    }

    private fun AppState.stateWithNewMessages() = this.copy(blogMessages = messages.value.toList())
}