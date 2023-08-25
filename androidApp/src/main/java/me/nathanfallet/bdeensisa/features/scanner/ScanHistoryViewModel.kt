package me.nathanfallet.bdeensisa.features.scanner

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
import me.nathanfallet.bdeensisa.models.ScanHistoryEntry

class ScanHistoryViewModel(
    application: Application,
    token: String?
) : AndroidViewModel(application) {

    // Properties

    private val entries = MutableLiveData<List<ScanHistoryEntry>>()
    private val grouped = MutableLiveData<Map<String, List<ScanHistoryEntry>>>()
    private val hasMore = MutableLiveData(true)

    // Getters

    fun getGrouped(): LiveData<Map<String, List<ScanHistoryEntry>>> {
        return grouped
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "scan_history")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ScanHistoryView")
        }

        fetchData(token, true)
    }

    fun fetchData(token: String?, reset: Boolean) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getScanHistory(
                        token,
                        (if (reset) 0 else entries.value?.size ?: 0).toLong()
                    ).let {
                        if (reset) {
                            entries.value = it
                        } else {
                            entries.value = (entries.value ?: listOf()) + it
                        }
                        hasMore.value = it.isNotEmpty()
                        grouped.value = entries.value?.groupBy { entry ->
                            "${
                                entry.scannedAt.toString().substring(0, 10)
                            }-${entry.event?.id ?: ""}"
                        }?.toSortedMap(reverseOrder())
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMore(token: String?, scannedAt: Instant) {
        if (hasMore.value != true) {
            return
        }
        if (entries.value?.lastOrNull()?.scannedAt == scannedAt) {
            fetchData(token, false)
        }
    }

}