package ru.nightgoat.secretblog.android

import android.app.Application
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.nightgoat.secretblog.data.DataBase
import ru.nightgoat.secretblog.di.dataModule
import ru.nightgoat.secretblog.models.BlogMessage

class BlogApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(dataModule)
        }
        MainScope().launch {
            val dataBase: DataBase<BlogMessage> by inject()
            dataBase.init(applicationContext.dataDir.path)
        }
    }
}