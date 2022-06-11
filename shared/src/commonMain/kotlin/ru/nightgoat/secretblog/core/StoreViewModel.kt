package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.models.BlogMessage

class StoreViewModel : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.Main),
    Store<AppState, BlogAction, BlogEffect> {

    private val dataBase: DataBase<BlogMessage> by inject()

    private var state = MutableStateFlow(AppState())
    private var sideEffect = MutableSharedFlow<BlogEffect>()

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
                launch {
                    state.value = reduceRefreshAction(action, oldState)
                    sideEffect.tryEmit(BlogEffect.ScrollToLastElement)
                }
            }
            is BlogAction.AddMessage -> {
                launch {
                    val newMessage = BlogMessage().apply {
                        text = action.message
                        isSecret = action.isSecret
                    }
                    addMessage(newMessage)
                }
            }
            is BlogAction.ClearDB -> {
                launch {
                    dataBase.deleteAll()
                    refresh(action = RefreshAction.DeleteAll)
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

    private fun reduceRefreshAction(
        action: BlogAction.Refresh,
        oldState: AppState,
    ): AppState {
        val oldMessages = oldState.blogMessages
        return when (val refreshAction = action.refreshAction) {
            is RefreshAction.Add -> {
                oldState.copy(blogMessages = oldMessages + refreshAction.message)
            }
            is RefreshAction.Delete -> {
                oldState.copy(blogMessages = oldMessages - refreshAction.message)
            }
            is RefreshAction.DeleteAll -> {
                oldState.copy(blogMessages = emptyList())
            }
        }
    }

    private suspend fun deleteMessage(message: BlogMessage) {
        dataBase.delete(message)
        refresh(RefreshAction.Delete(message))
    }

    private suspend fun addMessage(message: BlogMessage) {
        if (message.text.isNotEmpty()) {
            dataBase.add(message)
            refresh(RefreshAction.Add(message))
        }
    }

    fun clearDB() {
        dispatch(BlogAction.ClearDB)
    }

    private fun refresh(
        action: RefreshAction
    ) {
        dispatch(
            BlogAction.Refresh(
                refreshAction = action
            )
        )
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
}