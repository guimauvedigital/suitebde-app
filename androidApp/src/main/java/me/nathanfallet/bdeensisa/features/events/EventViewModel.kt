package me.nathanfallet.bdeensisa.features.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import me.nathanfallet.bdeensisa.models.Event

class EventViewModel(
    application: Application,
    event: Event,
) : AndroidViewModel(application) {

    // Properties

    private val event = MutableLiveData(event)

    // Getters

    fun getEvent(): LiveData<Event> {
        return event
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "event")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "EventView")
        }
    }

}