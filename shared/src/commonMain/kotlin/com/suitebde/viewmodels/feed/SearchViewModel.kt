package com.suitebde.viewmodels.feed

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.models.application.SearchOptions
import com.suitebde.models.clubs.Club
import com.suitebde.models.users.User
import com.suitebde.usecases.clubs.IFetchClubsUseCase
import com.suitebde.usecases.users.IFetchUsersUseCase
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val fetchUsersUseCase: IFetchUsersUseCase,
    private val fetchClubsUseCase: IFetchClubsUseCase,
) : ViewModel() {

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
