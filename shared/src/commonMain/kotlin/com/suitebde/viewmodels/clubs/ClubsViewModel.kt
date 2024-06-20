package com.suitebde.viewmodels.clubs

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.clubs.Club
import com.suitebde.usecases.clubs.IFetchClubsUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ClubsViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val fetchClubsUseCase: IFetchClubsUseCase,
) : ViewModel() {

    // Properties

    private val _clubs = MutableStateFlow<List<Club>?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val myClubs = _clubs.map {
        it?.filter { club -> club.isMember == true }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @NativeCoroutinesState
    val moreClubs = _clubs.map {
        it?.filter { club -> club.isMember != true } // Also includes "null"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    private var hasMore = true

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("clubs"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("ClubsView")
            )
        )

        fetchClubs()
    }

    @NativeCoroutines
    suspend fun fetchClubs(reset: Boolean = false) {
        try {
            _clubs.value = if (reset) fetchClubsUseCase(Pagination(25, 0), reset).also {
                hasMore = it.isNotEmpty()
            } else (_clubs.value ?: emptyList()) + fetchClubsUseCase(
                Pagination(25, _clubs.value?.size?.toLong() ?: 0), reset
            ).also {
                hasMore = it.isNotEmpty()
            }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun loadMoreIfNeeded(clubId: UUID) {
        if (!hasMore || _clubs.value?.lastOrNull()?.id != clubId) return
        viewModelScope.coroutineScope.launch {
            fetchClubs()
        }
    }

}
