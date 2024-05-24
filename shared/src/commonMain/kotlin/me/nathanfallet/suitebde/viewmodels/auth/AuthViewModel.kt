package me.nathanfallet.suitebde.viewmodels.auth

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.usecases.auth.IFetchTokenUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase

class AuthViewModel(
    environment: SuiteBDEEnvironment,
    private val fetchTokenUseCase: IFetchTokenUseCase,
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    val url = environment.baseUrl + "/auth/authorize?clientId=suitebde"

    @NativeCoroutines
    suspend fun authenticate(code: String, onUserLogged: () -> Unit) {
        try {
            fetchTokenUseCase(code) ?: return
            getCurrentUserUseCase()

            onUserLogged()
        } catch (e: APIException) {
            _error.value = e.message
        } catch (e: Exception) {
            _error.value = "auth_error_generic"
        }
    }

}
