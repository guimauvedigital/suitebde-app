package me.nathanfallet.suitebde.viewmodels.users

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase

class QRCodeViewModel(
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
) : KMMViewModel() {

    // Properties

    private val _user = MutableStateFlow<User?>(viewModelScope, null)

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    @NativeCoroutinesState
    val qrCodeUrl = _user.map {
        it?.let { "suitebde://users/${it.id}" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        fetchUser()
    }

    @NativeCoroutines
    suspend fun fetchUser() {
        _user.value = getCurrentUserUseCase()
    }

}
