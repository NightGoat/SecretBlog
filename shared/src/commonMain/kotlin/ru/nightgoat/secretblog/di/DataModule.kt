package ru.nightgoat.secretblog.di

import org.koin.dsl.module
import ru.nightgoat.secretblog.core.StoreViewModel
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.data.MessagesDataBase
import ru.nightgoat.secretblog.models.BlogMessage
import ru.nightgoat.secretblog.providers.DataBaseProvider
import ru.nightgoat.secretblog.providers.KvaultProvider
import ru.nightgoat.secretblog.providers.SettingsProvider

val dataModule = module {
    single { StoreViewModel() }
    single { DataBaseProvider() }
    single { KvaultProvider() }
    single { SettingsProvider() }
    single { MessagesDataBase(get()) as DataBase<BlogMessage> }
}