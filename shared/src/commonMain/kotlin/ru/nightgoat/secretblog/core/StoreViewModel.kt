package ru.nightgoat.secretblog.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nightgoat.secretblog.core.action.BlogAction
import ru.nightgoat.secretblog.core.action.RefreshAction
import ru.nightgoat.secretblog.core.reducers.mainReducer
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.SettingsProvider

class StoreViewModel : KoinComponent, CoroutineScope by CoroutineScope(Dispatchers.Main),
    Store<AppState, Action, BlogEffect> {

    val dataBase: DataBase<BlogMessage> by inject()
    val settingsProvider: SettingsProvider by inject()

    var state = MutableStateFlow(AppState())
    var sideEffect = MutableSharedFlow<BlogEffect>(extraBufferCapacity = 10)

    override fun observeState(): StateFlow<AppState> = state
    override fun observeSideEffect(): Flow<BlogEffect> = sideEffect
    override fun dispatch(action: Action) = mainReducer(action)

    fun addMessage(message: String, isSecret: Boolean = false) {
        message.takeIf { it.isNotEmpty() }?.let { newMessage ->
            val action = BlogAction.AddMessage(
                message = newMessage,
                isSecret = isSecret
            )

            dispatch(action)
        }
    }

    suspend fun addMessage(message: BlogMessage) {
        if (message.text.isNotEmpty()) {
            val newId = dataBase.add(message)
            refresh(RefreshAction.Add(message.copy(id = newId)))
        }
    }

    fun clearDB() {
        dispatch(BlogAction.ClearDB)
    }

    fun refresh(
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