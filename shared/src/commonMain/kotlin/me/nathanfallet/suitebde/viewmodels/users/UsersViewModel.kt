package me.nathanfallet.suitebde.viewmodels.users

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.users.IFetchUsersUseCase
import me.nathanfallet.usecases.pagination.Pagination

class UsersViewModel(
    private val fetchUsersUseCase: IFetchUsersUseCase,
) : KMMViewModel() {

    // TODO: Search

    // Properties

    private val _users = MutableStateFlow<List<User>?>(viewModelScope, null)

    @NativeCoroutinesState
    val users = _users.asStateFlow()

    private var hasMore = true

    // Methods

    @NativeCoroutines
    suspend fun fetchUsers(reset: Boolean = false) {
        _users.value = if (reset) fetchUsersUseCase(Pagination(25, 0)).also {
            hasMore = it.isNotEmpty()
        } else (_users.value ?: emptyList()) + fetchUsersUseCase(
            Pagination(25, users.value?.size?.toLong() ?: 0)
        ).also {
            hasMore = it.isNotEmpty()
        }
    }

    fun loadMoreIfNeeded(userId: String) {
        if (!hasMore || users.value?.lastOrNull()?.id != userId) return
        viewModelScope.coroutineScope.launch {
            fetchUsers()
        }
    }

}
