package ru.nightgoat.secretblog.core

import org.reduxkotlin.middleware
import ru.nightgoat.secretblog.models.BlogMessage

val blogMiddleware = middleware<BlogMessage> { store, next, action ->
    next(action)
}