package ru.nightgoat.secretblog.android.presentation.screens.chat

import ru.nightgoat.secretblog.providers.strings.Dictionary

class MessagesDropdowns(
    dictionary: Dictionary
) {
    val list = listOf(
        MessageDropDownSelectables.Edit(
            title = dictionary.edit
        ),
        MessageDropDownSelectables.Copy(
            title = dictionary.copy
        )
    )

    sealed class MessageDropDownSelectables {
        abstract val title: String

        class Edit(
            override val title: String
        ) : MessageDropDownSelectables()

        class Copy(
            override val title: String
        ) : MessageDropDownSelectables()
    }
}

