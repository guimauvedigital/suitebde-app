package me.nathanfallet.suitebde.viewmodels.root

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import io.ktor.http.*
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase
import me.nathanfallet.suitebde.usecases.auth.ILogoutUseCase

class RootViewModel(
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val logoutUseCase: ILogoutUseCase,
) : ViewModel() {

    // Properties

    private val _loading = MutableStateFlow(viewModelScope, false)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)
    private val _user = MutableStateFlow<User?>(viewModelScope, null)

    @NativeCoroutinesState
    val loading = _loading.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun fetchUser() {
        _loading.value = true
        _error.value = null
        try {
            _user.value = getCurrentUserUseCase()
        } catch (e: APIException) {
            if (e.code == HttpStatusCode.Unauthorized) {
                // Token is not valid anymore, remove it
                // TODO: Manage this in client
                logoutUseCase()
            } else {
                _error.value = e.message
            }
        } catch (e: Exception) {
            _error.value = "auth_error_generic"
        }
        _loading.value = false
    }

    fun logout() {
        logoutUseCase()
        _user.value = null
    }

}
