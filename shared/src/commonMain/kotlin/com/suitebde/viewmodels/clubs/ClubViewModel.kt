package com.suitebde.viewmodels.clubs

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.UserInClub
import com.suitebde.usecases.auth.IGetUserIdUseCase
import com.suitebde.usecases.clubs.IFetchClubUseCase
import com.suitebde.usecases.clubs.IListUsersInClubUseCase
import com.suitebde.usecases.clubs.IUpdateUserInClubUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClubViewModel(
    private val id: UUID?,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchClubUseCase: IFetchClubUseCase,
    private val listUsersInClubUseCase: IListUsersInClubUseCase,
    private val updateUserInClubUseCase: IUpdateUserInClubUseCase,
    private val getUserIdUseCase: IGetUserIdUseCase,
) : ViewModel() {

    // Properties

    private val _club = MutableStateFlow<Club?>(viewModelScope, null)
    private val _users = MutableStateFlow<List<UserInClub>?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val club = _club.asStateFlow()

    @NativeCoroutinesState
    val users = _users.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    private var hasMore = true

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
        try {
            _club.value = id?.let { fetchClubUseCase(it) }
            fetchUsers(reset)
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    @NativeCoroutines
    suspend fun fetchUsers(reset: Boolean = false) {
        id ?: return
        try {
            _users.value = if (reset) listUsersInClubUseCase(Pagination(25, 0), reset, id).also {
                hasMore = it.isNotEmpty()
            } else (_users.value ?: emptyList()) + listUsersInClubUseCase(
                Pagination(25, _users.value?.size?.toLong() ?: 0), reset, id
            ).also {
                hasMore = it.isNotEmpty()
            }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun loadMoreIfNeeded(userId: UUID) {
        if (!hasMore || _users.value?.lastOrNull()?.id != userId) return
        viewModelScope.coroutineScope.launch {
            fetchUsers()
        }
    }

    @NativeCoroutines
    suspend fun onJoinLeaveClicked() {
        val club = _club.value ?: return
        updateUserInClubUseCase(club)?.let { updatedClub ->
            _club.value = updatedClub.first
            updatedClub.second?.let { userInClub ->
                _users.value = (_users.value ?: emptyList()) + userInClub
            } ?: run {
                val myUserId = getUserIdUseCase()
                _users.value = _users.value?.filter { it.userId != myUserId }
            }
        }
    }

}
