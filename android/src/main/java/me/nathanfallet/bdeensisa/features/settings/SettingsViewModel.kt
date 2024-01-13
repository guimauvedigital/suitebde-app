package me.nathanfallet.bdeensisa.features.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import me.nathanfallet.bdeensisa.services.StorageService

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    // Properties

    private val eventsNotifications = MutableLiveData<Boolean>()

    // Getters

    fun getEventsNotifications(): LiveData<Boolean> {
        return eventsNotifications
    }

    // Setters

    fun setEventsNotifications(value: Boolean) {
        eventsNotifications.value = value
        StorageService.getInstance(getApplication()).sharedPreferences
            .edit()
            .putBoolean("notifications_events", value)
            .apply()
        updateNotifications(value, "events")
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "settings")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SettingsView")
        }

        eventsNotifications.value = StorageService.getInstance(application).sharedPreferences
            .getBoolean("notifications_events", true)
    }

    fun updateNotifications(enabled: Boolean, topic: String) {
        if (enabled) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        }
    }

}