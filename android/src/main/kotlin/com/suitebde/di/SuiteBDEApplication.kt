package com.suitebde.di

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.suitebde.BuildConfig
import com.suitebde.usecases.notifications.IUpdateFcmTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
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
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        if (!BuildConfig.DEBUG) initializeSentry()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            CoroutineScope(Dispatchers.IO).launch {
                get<IUpdateFcmTokenUseCase>().invoke(it)
            }
        }
    }

}
