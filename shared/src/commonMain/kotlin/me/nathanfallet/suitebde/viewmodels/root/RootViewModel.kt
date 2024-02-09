package me.nathanfallet.suitebde.viewmodels.root

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import io.ktor.http.*
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase
import me.nathanfallet.suitebde.usecases.auth.ISetTokenUseCase
import me.nathanfallet.suitebde.usecases.users.IFetchUserUseCase

class RootViewModel(
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val fetchUserUseCase: IFetchUserUseCase,
    private val setTokenUseCase: ISetTokenUseCase,
) : KMMViewModel() {

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
            getUserIdUseCase()?.let {
                _user.value = fetchUserUseCase(it)
            }
        } catch (e: APIException) {
            if (e.code == HttpStatusCode.Unauthorized) {
                // Token is not valid anymore, remove it
                // TODO: Manage this in client
                setTokenUseCase(null)
            } else {
                _error.value = e.message
            }
        } catch (e: Exception) {
            _error.value = "auth_error_generic"
        }
        _loading.value = false
    }

}
