package tw.tonyyang.englishwords

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.*
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.AppDatabase.Companion.getDatabase
import tw.tonyyang.englishwords.di.appModule
import tw.tonyyang.englishwords.di.databaseModule


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val debugBuild = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (debugBuild) {
            Timber.plant(DebugTree())
        }
        appContext = this
        startKoin {
            androidContext(this@App)
            modules(databaseModule)
            modules(appModule)
        }
        db = getDatabase(this)
    }

    companion object {
        lateinit var db: AppDatabase
        lateinit var appContext: Context
    }
}