package me.nathanfallet.suitebde.viewmodels.clubs

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase
import me.nathanfallet.suitebde.usecases.clubs.IFetchClubUseCase
import me.nathanfallet.suitebde.usecases.clubs.IListUsersInClubUseCase
import me.nathanfallet.suitebde.usecases.clubs.IUpdateUserInClubUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import me.nathanfallet.usecases.pagination.Pagination

class ClubViewModel(
    private val id: String?,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchClubUseCase: IFetchClubUseCase,
    private val listUsersInClubUseCase: IListUsersInClubUseCase,
    private val updateUserInClubUseCase: IUpdateUserInClubUseCase,
    private val getUserIdUseCase: IGetUserIdUseCase,
) : KMMViewModel() {

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

    fun loadMoreIfNeeded(userId: String) {
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
