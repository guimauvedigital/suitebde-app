package me.nathanfallet.bdeensisa.features.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.models.Notification
import me.nathanfallet.bdeensisa.models.NotificationPayload
import me.nathanfallet.bdeensisa.services.APIService

class SendNotificationViewModel(application: Application) : AndroidViewModel(application) {

    // Properties

    private val topic = MutableLiveData("broadcast")
    private val title = MutableLiveData("")
    private val body = MutableLiveData("")
    private val sent = MutableLiveData(false)

    // Getters

    fun getTopic(): LiveData<String> {
        return topic
    }

    fun getTitle(): LiveData<String> {
        return title
    }

    fun getBody(): LiveData<String> {
        return body
    }

    fun getSent(): LiveData<Boolean> {
        return sent
    }

    // Setters

    fun setTopic(value: String) {
        topic.value = value
    }

    fun setTitle(value: String) {
        title.value = value
    }

    fun setBody(value: String) {
        body.value = value
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "send_notification")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SendNotificationView")
        }
    }

    fun send(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                APIService().sendNotification(
                    token, NotificationPayload(
                        null,
                        topic.value ?: "broadcast",
                        Notification(
                            title.value ?: "",
                            body.value ?: ""
                        )
                    )
                )
                sent.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dismissSent() {
        sent.value = false
    }

}