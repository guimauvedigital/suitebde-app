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
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.models.EventUpload
import me.nathanfallet.bdeensisa.views.AlertCase

class EventViewModel(
    application: Application,
    event: Event?,
    val editable: Boolean
) : AndroidViewModel(application) {

    // Properties

    private val event = MutableLiveData(event)
    private val editing = MutableLiveData(event == null)

    private val hasUnsavedChanges = MutableLiveData(false)
    private val alert = MutableLiveData<AlertCase?>()

    private val title = MutableLiveData<String>()
    private val start = MutableLiveData<Instant>()
    private val end = MutableLiveData<Instant>()
    private val content = MutableLiveData<String>()
    private val validated = MutableLiveData<Boolean>()

    // Getters

    fun getEvent(): LiveData<Event?> {
        return event
    }

    fun isEditing(): LiveData<Boolean> {
        return editing
    }

    fun getAlert(): LiveData<AlertCase?> {
        return alert
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

    fun setAlert(alert: AlertCase?) {
        if (this.alert.value == AlertCase.SAVED && alert == null) {
            hasUnsavedChanges.value = false
        }
        this.alert.value = alert
    }

    fun setTitle(value: String) {
        title.value = value
        this.hasUnsavedChanges.value = true
    }

    fun setStart(value: Instant) {
        start.value = value
        this.hasUnsavedChanges.value = true
    }

    fun setEnd(value: Instant) {
        end.value = value
        this.hasUnsavedChanges.value = true
    }

    fun setContent(value: String) {
        content.value = value
        this.hasUnsavedChanges.value = true
    }

    fun setValidated(value: Boolean) {
        validated.value = value
        this.hasUnsavedChanges.value = true
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "event")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "EventView")
        }

        resetChanges()
    }

    private fun resetChanges() {
        title.value = event.value?.title
        start.value = event.value?.start
        end.value = event.value?.end
        content.value = event.value?.content
        validated.value = event.value?.validated

        hasUnsavedChanges.value = false
    }

    fun toggleEdit() {
        if (editable) {
            if (editing.value == true && hasUnsavedChanges.value == true) {
                setAlert(AlertCase.CANCELLING)
                return
            }
            editing.value = !(editing.value ?: false)
            if (editing.value == true) {
                resetChanges()
            }
        }
    }

    fun discardEdit() {
        editing.value = false
    }

    fun updateInfo(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val newEvent = if (event.value != null) SharedCacheService.getInstance(
                    DatabaseDriverFactory(getApplication())
                ).apiService().updateEvent(
                    token,
                    event.value!!.id,
                    EventUpload(
                        title.value ?: "",
                        content.value ?: "",
                        start.value?.toString() ?: "",
                        end.value?.toString() ?: "",
                        event.value?.topicId,
                        validated.value ?: false
                    )
                ) else SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().suggestEvent(
                        token,
                        EventUpload(
                            title.value ?: "",
                            content.value ?: "",
                            start.value?.toString() ?: "",
                            end.value?.toString() ?: "",
                            event.value?.topicId
                        )
                    )
                event.value = newEvent
                setAlert(AlertCase.SAVED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}