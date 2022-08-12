package ru.nightgoat.secretblog.android.presentation.screens.chat

import androidx.annotation.DrawableRes
import ru.nightgoat.secretblog.android.R
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.strings.Dictionary

class MessagesDropdowns(
    private val dictionary: Dictionary
) {
    fun list(message: BlogMessage) = listOf(
        MessageDropDownSelectables.Edit(
            title = dictionary.edit,
            iconId = R.drawable.ic_outline_edit_24
        ),
        MessageDropDownSelectables.Copy(
            title = dictionary.copy,
            iconId = R.drawable.ic_outline_content_copy_24
        ),
        if (message.isSecret) {
            MessageDropDownSelectables.RevealSecret(
                title = dictionary.revealMessage,
                iconId = R.drawable.ic_outline_visibility_24,
            )
        } else {
            MessageDropDownSelectables.MakeSecret(
                title = dictionary.makeSecret,
                iconId = R.drawable.ic_outline_visibility_off_24,
            )
        },
        MessageDropDownSelectables.Delete(
            title = dictionary.delete,
            iconId = R.drawable.ic_outline_delete_24
        )
    )

    sealed class MessageDropDownSelectables {
        abstract val title: String
        abstract val iconId: Int

        class Edit(
            override val title: String,
            @DrawableRes override val iconId: Int
        ) : MessageDropDownSelectables()

        class Copy(
            override val title: String,
            @DrawableRes override val iconId: Int
        ) : MessageDropDownSelectables()

        class Delete(
            override val title: String,
            @DrawableRes override val iconId: Int
        ) : MessageDropDownSelectables()

        class MakeSecret(
            override val title: String,
            @DrawableRes override val iconId: Int,
        ) : MessageDropDownSelectables()

        class RevealSecret(
            override val title: String,
            @DrawableRes override val iconId: Int,
        ) : MessageDropDownSelectables()
    }
}

