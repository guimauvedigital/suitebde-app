package me.nathanfallet.suitebde.viewmodels.clubs

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class ClubsViewModel(
    private val logEventUseCase: ILogEventUseCase,
) : KMMViewModel() {

    // Properties

    private val _clubs = MutableStateFlow<List<Club>?>(viewModelScope, null)

    @NativeCoroutinesState
    val myClubs = _clubs.map {
        it?.filter { club -> club.isMember == true }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @NativeCoroutinesState
    val moreClubs = _clubs.map {
        it?.filter { club -> club.isMember != true } // Also includes "null"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

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
        // TODO

        // For testing purposes
        _clubs.value = listOf(
            Club(
                id = "id",
                associationId = "associationId",
                name = "Club running",
                description = "",
                logo = "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
                createdAt = Clock.System.now(),
                validated = true,
                usersCount = 12,
                isMember = true
            )
        )
    }

    fun loadMoreIfNeeded(clubId: String) {
        // TODO
    }

}
