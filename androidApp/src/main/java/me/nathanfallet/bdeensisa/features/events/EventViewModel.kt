package me.nathanfallet.bdeensisa.features.events

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
import kotlinx.datetime.Instant
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.services.APIService

class EventViewModel(
    application: Application,
    event: Event?,
    val editable: Boolean
) : AndroidViewModel(application) {

    // Properties

    private val event = MutableLiveData(event)
    private val editing = MutableLiveData(editable && event == null)

    private val title = MutableLiveData<String>(event?.title)
    private val start = MutableLiveData<Instant>(event?.start)
    private val end = MutableLiveData<Instant>(event?.end)
    private val content = MutableLiveData<String>(event?.content)
    private val validated = MutableLiveData<Boolean>(event?.validated)

    // Getters

    fun getEvent(): LiveData<Event?> {
        return event
    }

    fun isEditing(): LiveData<Boolean> {
        return editing
    }

    fun getTitle(): LiveData<String> {
        return title
    }

    fun getStart(): LiveData<Instant> {
        return start
    }

    fun getEnd(): LiveData<Instant> {
        return end
    }

    fun getContent(): LiveData<String> {
        return content
    }

    fun isValidated(): LiveData<Boolean> {
        return validated
    }

    // Setters

    fun setTitle(value: String) {
        title.value = value
    }

    fun setStart(value: Instant) {
        start.value = value
    }

    fun setEnd(value: Instant) {
        end.value = value
    }

    fun setContent(value: String) {
        content.value = value
    }

    fun setValidated(value: Boolean) {
        validated.value = value
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "event")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "EventView")
        }
    }

    fun toggleEdit() {
        editing.value = !(editing.value ?: false)
    }

    fun updateInfo(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val newEvent = if (event.value != null) APIService().updateEvent(
                    token,
                    event.value!!.id,
                    title.value ?: "",
                    start.value?.toString() ?: "",
                    end.value?.toString() ?: "",
                    content.value ?: "",
                    event.value?.topicId,
                    validated.value ?: false
                ) else APIService().createEvent(
                    token,
                    title.value ?: "",
                    start.value?.toString() ?: "",
                    end.value?.toString() ?: "",
                    content.value ?: "",
                    event.value?.topicId,
                    validated.value ?: false
                )
                event.value = newEvent
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}