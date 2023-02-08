package me.nathanfallet.bdeensisa.features.manage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class ManageViewModel(application: Application) : AndroidViewModel(application) {

    fun load(): ManageViewModel {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "manage")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ManageView")
        }

        return this
    }

}