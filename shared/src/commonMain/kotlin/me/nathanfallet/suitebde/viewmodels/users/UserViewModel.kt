package me.nathanfallet.suitebde.viewmodels.users

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.application.AlertCase
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.users.IFetchUserUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class UserViewModel(
    private val associationId: String,
    private val userId: String,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchUserUseCase: IFetchUserUseCase,
) : KMMViewModel() {

    // Properties

    private val _user = MutableStateFlow<User?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    private val _isEditing = MutableStateFlow(viewModelScope, false)
    private val _alert = MutableStateFlow<AlertCase?>(viewModelScope, null)

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val isEditing = _isEditing.asStateFlow()

    @NativeCoroutinesState
    val alert = _alert.asStateFlow()

    private var hasUnsavedChanges = false

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("club"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("ClubView")
            )
        )

        fetchUser()
    }

    @NativeCoroutines
    suspend fun fetchUser() {
        try {
            _user.value = fetchUserUseCase(userId, associationId)
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

}
