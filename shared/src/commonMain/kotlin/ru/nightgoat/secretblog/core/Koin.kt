package ru.nightgoat.secretblog.core

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import ru.nightgoat.secretblog.di.dataModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(dataModule)
}

fun initKoin() = initKoin {}