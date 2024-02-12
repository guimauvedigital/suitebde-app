package me.nathanfallet.suitebde.viewmodels.clubs

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class ClubViewModel(
    private val id: String?,
    private val logEventUseCase: ILogEventUseCase,
) : KMMViewModel() {

    // Properties

    private val _club = MutableStateFlow<Club?>(viewModelScope, null)
    private val _users = MutableStateFlow<List<UserInClub>?>(viewModelScope, null)

    @NativeCoroutinesState
    val club = _club.asStateFlow()

    @NativeCoroutinesState
    val users = _users.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("club"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("ClubView")
            )
        )

        fetchClub()
    }

    @NativeCoroutines
    suspend fun fetchClub(reset: Boolean = false) {
        // TODO

        // For testing purposes
        _club.value = Club(
            id = "id",
            associationId = "associationId",
            name = "Club running",
            description = "Club running de l'ENSISA ! RDV tous les jeudis à la barrière du parking Werner",
            logo = "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 12,
            isMember = true
        )

        fetchUsers(reset)
    }

    @NativeCoroutines
    suspend fun fetchUsers(reset: Boolean = false) {
        // TODO
    }

    @NativeCoroutines
    suspend fun join() {
        // TODO (replace by real fetch)
        _club.value = _club.value?.copy(usersCount = (_club.value?.usersCount ?: 0) + 1, isMember = true)
    }

    @NativeCoroutines
    suspend fun leave() {
        // TODO (replace by real fetch)
        _club.value = _club.value?.copy(usersCount = (_club.value?.usersCount ?: 0) - 1, isMember = false)
    }

}
