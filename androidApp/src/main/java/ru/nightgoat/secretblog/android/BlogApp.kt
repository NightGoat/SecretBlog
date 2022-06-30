package ru.nightgoat.secretblog.android

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.nightgoat.kexcore.utils.Kex
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.nightgoat.secretblog.di.dataModule
import ru.nightgoat.secretblog.utils.NapierKexLogger

class BlogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(dataModule)
        }
        Napier.base(DebugAntilog())
        Kex.setCustomLogger(NapierKexLogger)
    }
}