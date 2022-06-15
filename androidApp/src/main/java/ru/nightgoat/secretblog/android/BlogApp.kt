package ru.nightgoat.secretblog.android

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.nightgoat.secretblog.di.dataModule

class BlogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(dataModule)
        }
        Napier.base(DebugAntilog())
    }
}