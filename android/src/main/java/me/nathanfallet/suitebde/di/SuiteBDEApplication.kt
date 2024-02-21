package me.nathanfallet.suitebde.di

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import me.nathanfallet.suitebde.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class SuiteBDEApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@SuiteBDEApplication)
            modules(sharedModule + androidModule)
        }
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(
            !BuildConfig.DEBUG
        )
        if (!BuildConfig.DEBUG) {
            initializeSentry(this)
        }
    }

}
