package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.models.BlogMessage

class StoreViewModel : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.Main),
    Store<AppState, BlogAction, BlogEffect> {

    private val dataBase: DataBase<BlogMessage> by inject()

    private val state = MutableStateFlow(AppState())
    private val sideEffect = MutableSharedFlow<BlogEffect>()
    private val messages = dataBase.flow.stateIn(this, SharingStarted.Eagerly, emptyList())

    init {
        launch {
            dataBase.init()
        }
    }

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
            is BlogAction.RemoveMessages -> {
                launch {
                    action.messages.forEach { message ->
                        deleteMessage(message)
                    }
                }
            }
            is BlogAction.ReverseSecretBlogsVisibility -> {
                state.value = oldState.reversedVisibility
            }
            is BlogAction.ReverseEditMode -> {
                state.value = oldState.reversedEdit
            }
            is BlogAction.SelectMessage -> {
                state.value = oldState.copy(
                    blogMessages = oldState.blogMessages.map { message ->
                        if (message.id == action.message.id) {
                            message.copy(isSelected = action.isSelected)
                        } else {
                            message
                        }
                    }
                )
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

    fun reverseVisibility() {
        dispatch(BlogAction.ReverseSecretBlogsVisibility)
    }

    fun reverseEditMode() {
        dispatch(BlogAction.ReverseEditMode)
    }

    fun onMessageSelected(message: BlogMessage, isSelected: Boolean) {
        dispatch(BlogAction.SelectMessage(message, isSelected))
    }

    fun deleteSelectedMessages() {
        dispatch(BlogAction.RemoveMessages(state.value.visibleMessages.filter { it.isSelected }))
        dispatch(BlogAction.ReverseEditMode)
    }

    private fun AppState.stateWithNewMessages() = this.copy(blogMessages = messages.value.toList())
}