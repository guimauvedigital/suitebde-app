package me.nathanfallet.suitebde.viewmodels.feed

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.application.SearchOptions
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.clubs.IFetchClubsUseCase
import me.nathanfallet.suitebde.usecases.users.IFetchUsersUseCase
import me.nathanfallet.usecases.pagination.Pagination

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val fetchUsersUseCase: IFetchUsersUseCase,
    private val fetchClubsUseCase: IFetchClubsUseCase,
) : KMMViewModel() {

    // Properties

    private val _search = MutableStateFlow(viewModelScope, "")

    private val _users = MutableStateFlow<List<User>?>(viewModelScope, null)
    private val _clubs = MutableStateFlow<List<Club>?>(viewModelScope, null)

    private val _hasMoreUsers = MutableStateFlow(viewModelScope, true)
    private val _hasMoreClubs = MutableStateFlow(viewModelScope, true)

    @NativeCoroutinesState
    val search = _search.asStateFlow()

    @NativeCoroutinesState
    val users = _users.asStateFlow()

    @NativeCoroutinesState
    val clubs = _clubs.asStateFlow()

    @NativeCoroutinesState
    val hasMoreUsers = _hasMoreUsers.asStateFlow()

    @NativeCoroutinesState
    val hasMoreClubs = _hasMoreClubs.asStateFlow()

    // Setters

    fun updateSearch(search: String) {
        _search.value = search
        if (search.isBlank()) {
            _users.value = null
            _clubs.value = null
        }
    }

    // Methods

    init {
        viewModelScope.coroutineScope.launch {
            _search.debounce(500L).collect {
                fetchUsers(true)
                fetchClubs(true)
            }
        }
    }

    @NativeCoroutines
    suspend fun fetchUsers(reset: Boolean = false) {
        val search = search.value.trim().takeIf { it.isNotBlank() } ?: run {
            _users.value = null
            return
        }
        _users.value = if (reset) fetchUsersUseCase(Pagination(25, 0, SearchOptions(search))).also {
            _hasMoreUsers.value = it.isNotEmpty()
        } else (_users.value ?: emptyList()) + fetchUsersUseCase(
            Pagination(25, users.value?.size?.toLong() ?: 0, SearchOptions(search))
        ).also {
            _hasMoreUsers.value = it.isNotEmpty()
        }
    }

    fun loadMoreUsers() {
        if (!hasMoreUsers.value) return
        viewModelScope.coroutineScope.launch {
            fetchUsers()
        }
    }

    @NativeCoroutines
    suspend fun fetchClubs(reset: Boolean = false) {
        val search = search.value.trim().takeIf { it.isNotBlank() } ?: run {
            _clubs.value = null
            return
        }
        _clubs.value = if (reset) fetchClubsUseCase(Pagination(25, 0, SearchOptions(search)), reset).also {
            _hasMoreClubs.value = it.isNotEmpty()
        } else (_clubs.value ?: emptyList()) + fetchClubsUseCase(
            Pagination(25, clubs.value?.size?.toLong() ?: 0, SearchOptions(search)), reset
        ).also {
            _hasMoreClubs.value = it.isNotEmpty()
        }
    }

    fun loadMoreClubs() {
        if (!hasMoreClubs.value) return
        viewModelScope.coroutineScope.launch {
            fetchClubs()
        }
    }

}
