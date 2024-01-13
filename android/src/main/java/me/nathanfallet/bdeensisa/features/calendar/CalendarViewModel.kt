package me.nathanfallet.bdeensisa.features.calendar

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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.models.CalendarEvent
import kotlin.time.Duration.Companion.days

class CalendarViewModel(
    application: Application,
    token: String?
) : AndroidViewModel(application) {

    // Properties

    private val day = MutableLiveData(Clock.System.now())
    private val events = MutableLiveData<List<CalendarEvent>>()
    private val todayEvents = MutableLiveData<List<CalendarEvent>>()

    // Getters

    fun getDay(): LiveData<Instant> {
        return day
    }

    fun getTodayEvents(): LiveData<List<CalendarEvent>> {
        return todayEvents
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "calendar")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "CalendarView")
        }

        fetchData(token, false)
    }

    fun fetchData(token: String?, reload: Boolean) {
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .getEvents(token, reload)
                    .let {
                        events.value = it
                        updateTodayEvents()
                    }
                if (!reload) {
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .getEvents(token, true)
                        .let {
                            events.value = it
                            updateTodayEvents()
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTodayEvents() {
        val todayLocal = day.value?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
        todayEvents.value = events.value?.filter {
            it.start?.toLocalDateTime(TimeZone.currentSystemDefault())?.date == todayLocal ||
                    it.end?.toLocalDateTime(TimeZone.currentSystemDefault())?.date == todayLocal
        }
    }

    fun previous() {
        day.value = day.value?.minus(1.days) ?: Clock.System.now()
        updateTodayEvents()
    }

    fun next() {
        day.value = day.value?.plus(1.days) ?: Clock.System.now()
        updateTodayEvents()
    }

}