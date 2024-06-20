package com.suitebde.viewmodels.scans

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.scans.ScansForDay
import com.suitebde.usecases.scans.IFetchScansUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*

class ScanHistoryViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val fetchScansUseCase: IFetchScansUseCase,
) : ViewModel() {

    // Properties

    private val _scans = MutableStateFlow<List<ScansForDay>?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val scans = _scans.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    private var hasMore = true

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("scan_history"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("ScanHistoryView")
            )
        )

        fetchScansForDays()
    }

    @NativeCoroutines
    suspend fun fetchScansForDays(reset: Boolean = false) {
        try {
            val endsDate = _scans.value?.takeIf { !reset }?.minBy { it.date }?.date
                ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(1, DateTimeUnit.DAY)
            val startsDate = endsDate.minus(1, DateTimeUnit.MONTH)

            _scans.value = (_scans.value?.takeIf { !reset } ?: emptyList()) + fetchScansUseCase(startsDate, endsDate)
                .also { hasMore = it.isNotEmpty() }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun loadMoreIfNeeded(date: LocalDate) {
        if (!hasMore || _scans.value?.lastOrNull()?.date != date) return
        viewModelScope.coroutineScope.launch {
            fetchScansForDays()
        }
    }

}
