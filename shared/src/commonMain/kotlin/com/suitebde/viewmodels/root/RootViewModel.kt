package com.suitebde.viewmodels.root

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.models.application.ScannedUser
import com.suitebde.models.application.Url
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetCurrentUserUseCase
import com.suitebde.usecases.scans.ILogScanUseCase
import com.suitebde.usecases.users.IDeleteUserUseCase
import dev.kaccelero.commons.auth.ILogoutUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.models.UUID
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RootViewModel(
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val logoutUseCase: ILogoutUseCase,
    private val deleteUserUseCase: IDeleteUserUseCase,
    private val logScanUseCase: ILogScanUseCase,
) : ViewModel() {

    // Properties

    private val _loading = MutableStateFlow(viewModelScope, false)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)
    private val _user = MutableStateFlow<User?>(viewModelScope, null)

    private val _scannedUser = MutableStateFlow<ScannedUser?>(viewModelScope, null)

    @NativeCoroutinesState
    val loading = _loading.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    @NativeCoroutinesState
    val scannedUser = _scannedUser.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun fetchUser() {
        _loading.value = true
        _error.value = null
        try {
            _user.value = getCurrentUserUseCase()
        } catch (e: APIException) {
            _error.value = e.message
        } catch (e: Exception) {
            _error.value = "auth_error_generic"
        }
        _loading.value = false
    }

    fun logout() {
        logoutUseCase()
        _user.value = null
    }

    fun deleteAccount() {
        val userId = _user.value?.id ?: return
        viewModelScope.coroutineScope.launch {
            deleteUserUseCase(userId, null)
        }
        logoutUseCase()
        _user.value = null
    }

    fun onOpenURL(url: Url) {
        // We don't open URLs with no path
        if (url.path == null) return

        // Check url for sharable data
        if (url.scheme == "suitebde") when (url.host) {
            "users" -> url.pathSegments?.takeIf { it.size >= 2 }?.let {
                _scannedUser.value = ScannedUser(it[0].let(::UUID), it[1].let(::UUID))
                viewModelScope.coroutineScope.launch {
                    logScanUseCase(CreateScanPayload(it[1].let(::UUID)))
                }
            }
        }
    }

    fun closeItem() {
        _scannedUser.value = null
    }

}
