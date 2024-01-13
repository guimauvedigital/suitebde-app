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

        fetchData(token)
    }

    fun fetchData(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                entries.value =
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService()
                        .getScanHistory(token)
                grouped.value = entries.value?.groupBy { entry ->
                    "${entry.scannedAt.toString().substring(0, 10)}-${entry.event?.id ?: ""}"
                }?.toSortedMap(reverseOrder())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}