package uk.ac.tees.mad.dailywords

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import uk.ac.tees.mad.dailywords.di.appModule

class DailyWordsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DailyWordsApplication)
            modules(appModule)
        }
    }
}
