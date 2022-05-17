package ru.nightgoat.secretblog.di

import org.koin.dsl.module
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.data.MessagesDataBase
import ru.nightgoat.secretblog.models.BlogMessage

val dataModule = module {
    single { StoreViewModel() }
    single { MessagesDataBase as DataBase<BlogMessage> }
}