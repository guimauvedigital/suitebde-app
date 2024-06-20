package com.suitebde.viewmodels.users

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetCurrentUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class QRCodeViewModel(
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
) : ViewModel() {

    // Properties

    private val _user = MutableStateFlow<User?>(viewModelScope, null)

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    @NativeCoroutinesState
    val qrCodeUrl = _user.map {
        it?.let { "suitebde://users/${it.associationId}/${it.id}" }
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
