package com.suitebde.viewmodels.auth

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.application.SuiteBDEEnvironment
import com.suitebde.usecases.auth.IFetchTokenUseCase
import com.suitebde.usecases.auth.IGetCurrentUserUseCase
import dev.kaccelero.commons.exceptions.APIException
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    environment: SuiteBDEEnvironment,
    private val fetchTokenUseCase: IFetchTokenUseCase,
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    val url = environment.baseUrl + "/auth/authorize?clientId=00000000-0000-0000-0000-000000000000"

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
