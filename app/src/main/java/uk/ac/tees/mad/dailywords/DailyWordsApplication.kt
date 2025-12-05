package uk.ac.tees.mad.dailywords

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uk.ac.tees.mad.dailywords.di.appModule
import uk.ac.tees.mad.dailywords.di.profileModule
import uk.ac.tees.mad.dailywords.di.supabaseModule
import uk.ac.tees.mad.dailywords.di.viewModelModule
import uk.ac.tees.mad.dailywords.ui.di.repositoryModule

class DailyWordsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DailyWordsApplication)
            modules(appModule, viewModelModule, profileModule, supabaseModule, repositoryModule)
        }
    }
}
