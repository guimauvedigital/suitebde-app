package com.suitebde.viewmodels.users

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.application.AlertCase
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetCurrentUserUseCase
import com.suitebde.usecases.users.IFetchSubscriptionsInUsersUseCase
import com.suitebde.usecases.users.IFetchUserUseCase
import com.suitebde.usecases.users.IUpdateUserUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.models.UUID
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(
    private val associationId: UUID,
    private val userId: UUID,
    private val logEventUseCase: ILogEventUseCase,
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val fetchUserUseCase: IFetchUserUseCase,
    private val fetchSubscriptionsInUsersUseCase: IFetchSubscriptionsInUsersUseCase,
    private val updateUserUseCase: IUpdateUserUseCase,
) : ViewModel() {

    // Properties

    private val _user = MutableStateFlow<User?>(viewModelScope, null)
    private val _subscriptions = MutableStateFlow<List<SubscriptionInUser>?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    private val _firstName = MutableStateFlow(viewModelScope, "")
    private val _lastName = MutableStateFlow(viewModelScope, "")

    private val _isCurrentUser = MutableStateFlow(viewModelScope, false)
    private val _isEditing = MutableStateFlow(viewModelScope, false)
    private val _alert = MutableStateFlow<AlertCase?>(viewModelScope, null)
    private val _isEditable = MutableStateFlow(viewModelScope, false)
    private val _isAllEditable = MutableStateFlow(viewModelScope, false)

    @NativeCoroutinesState
    val user = _user.asStateFlow()

    @NativeCoroutinesState
    val subscriptions = _subscriptions.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val firstName = _firstName.asStateFlow()

    @NativeCoroutinesState
    val lastName = _lastName.asStateFlow()

    @NativeCoroutinesState
    val isCurrentUser = _isCurrentUser.asStateFlow()

    @NativeCoroutinesState
    val isEditing = _isEditing.asStateFlow()

    @NativeCoroutinesState
    val alert = _alert.asStateFlow()

    @NativeCoroutinesState
    val isEditable = _isEditable.asStateFlow()

    @NativeCoroutinesState
    val isAllEditable = _isAllEditable.asStateFlow()

    private var hasUnsavedChanges = false

    // Setters

    fun updateFirstName(value: String) {
        _firstName.value = value
        hasUnsavedChanges = true
    }

    fun updateLastName(value: String) {
        _lastName.value = value
        hasUnsavedChanges = true
    }

    fun setAlert(value: AlertCase?) {
        if (_alert.value == AlertCase.SAVED && value == null) {
            hasUnsavedChanges = false
        }
        _alert.value = value
    }

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("user"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("UserView")
            )
        )

        fetchUser()
    }

    @NativeCoroutines
    suspend fun fetchUser() {
        try {
            _user.value = fetchUserUseCase(userId, associationId)?.also {
                fetchPermissions(it)
            }
            _subscriptions.value = fetchSubscriptionsInUsersUseCase(userId, associationId)
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    @NativeCoroutines
    suspend fun fetchPermissions(user: User) {
        try {
            // Check for current user it the association of the target user (as scan is association-wide)
            val currentUser = getCurrentUserUseCase() ?: return
            _isCurrentUser.value = user.id == currentUser.id
            _isAllEditable.value = checkPermissionUseCase(
                currentUser, Permission.USERS_UPDATE inAssociation user.associationId
            )
            _isEditable.value = _isCurrentUser.value || _isAllEditable.value
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetChanges() {
        _firstName.value = user.value?.firstName ?: ""
        _lastName.value = user.value?.lastName ?: ""
        hasUnsavedChanges = false
    }

    fun toggleEditing() {
        if (!_isEditable.value) return
        if (_isEditing.value && hasUnsavedChanges) {
            setAlert(AlertCase.CANCELLING)
            return
        }
        _isEditing.value = !_isEditing.value
        if (_isEditing.value) resetChanges()
    }

    fun discardEditingFromAlert() {
        _isEditing.value = false
    }

    @NativeCoroutines
    suspend fun saveChanges() {
        try {
            _user.value = updateUserUseCase(
                userId, associationId, UpdateUserPayload(
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    password = null
                )
            )
            setAlert(AlertCase.SAVED)
        } catch (e: APIException) {
            e.printStackTrace()
            setAlert(AlertCase.ERROR)
            // TODO: Set error message
        }
    }

}
