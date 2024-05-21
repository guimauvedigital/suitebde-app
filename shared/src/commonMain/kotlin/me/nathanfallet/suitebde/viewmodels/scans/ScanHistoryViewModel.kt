package me.nathanfallet.suitebde.viewmodels.scans

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.*
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.suitebde.usecases.scans.IFetchScansUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

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
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.plus(1, DateTimeUnit.DAY)
            val aMonthAgo = now.minus(1, DateTimeUnit.MONTH)
            _scans.value = if (reset) fetchScansUseCase(aMonthAgo, now).also {
                hasMore = it.isNotEmpty()
            } else (_scans.value ?: emptyList()) + fetchScansUseCase(aMonthAgo, now).also {
                hasMore = it.isNotEmpty()
            }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

}
