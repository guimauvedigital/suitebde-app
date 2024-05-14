package me.nathanfallet.suitebde.viewmodels.events

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.application.AlertCase
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase
import me.nathanfallet.suitebde.usecases.events.ICreateEventUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventUseCase
import me.nathanfallet.suitebde.usecases.events.IUpdateEventUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class EventViewModel(
    private val id: String?,
    private val logEventUseCase: ILogEventUseCase,
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val fetchEventUseCase: IFetchEventUseCase,
    private val createEventUseCase: ICreateEventUseCase,
    private val updateEventUseCase: IUpdateEventUseCase,
) : ViewModel() {

    // Properties

    private val _event = MutableStateFlow<Event?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    private val _name = MutableStateFlow(viewModelScope, "")
    private val _description = MutableStateFlow(viewModelScope, "")
    private val _startsAt = MutableStateFlow(viewModelScope, Clock.System.now())
    private val _endsAt = MutableStateFlow(viewModelScope, Clock.System.now())
    private val _validated = MutableStateFlow(viewModelScope, false)

    private val _isEditing = MutableStateFlow(viewModelScope, id == null)
    private val _alert = MutableStateFlow<AlertCase?>(viewModelScope, null)
    private val _isEditable = MutableStateFlow(viewModelScope, false)

    @NativeCoroutinesState
    val event = _event.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val name = _name.asStateFlow()

    @NativeCoroutinesState
    val description = _description.asStateFlow()

    @NativeCoroutinesState
    val startsAt = _startsAt.asStateFlow()

    @NativeCoroutinesState
    val endsAt = _endsAt.asStateFlow()

    @NativeCoroutinesState
    val validated = _validated.asStateFlow()

    @NativeCoroutinesState
    val isEditing = _isEditing.asStateFlow()

    @NativeCoroutinesState
    val alert = _alert.asStateFlow()

    @NativeCoroutinesState
    val isEditable = _isEditable.asStateFlow()

    private var hasUnsavedChanges = false

    // Setters

    fun updateName(value: String) {
        _name.value = value
        hasUnsavedChanges = true
    }

    fun updateDescription(value: String) {
        _description.value = value
        hasUnsavedChanges = true
    }

    fun updateStartsAt(value: Instant) {
        _startsAt.value = value
        hasUnsavedChanges = true
    }

    fun updateEndsAt(value: Instant) {
        _endsAt.value = value
        hasUnsavedChanges = true
    }

    fun updateValidated(value: Boolean) {
        _validated.value = value
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
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("event"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("EventView")
            )
        )

        fetchEvent()
    }

    @NativeCoroutines
    suspend fun fetchEvent(reset: Boolean = false) {
        try {
            _event.value = id?.let { fetchEventUseCase(id, reset) }?.also {
                fetchPermissions(it)
            }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    @NativeCoroutines
    suspend fun fetchPermissions(event: Event) {
        if (id == null) {
            // Edit is enabled but cannot be turned off
            _isEditable.value = false
            return
        }
        try {
            val currentUser = getCurrentUserUseCase() ?: return
            _isEditable.value = checkPermissionUseCase(
                currentUser, Permission.EVENTS_UPDATE inAssociation event.associationId
            )
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun resetChanges() {
        _name.value = event.value?.name ?: ""
        _description.value = event.value?.description ?: ""
        _startsAt.value = event.value?.startsAt ?: Instant.DISTANT_PAST
        _endsAt.value = event.value?.endsAt ?: Instant.DISTANT_PAST
        _validated.value = event.value?.validated ?: false
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
            _event.value = id?.let {
                updateEventUseCase(
                    id, UpdateEventPayload(
                        name = _name.value,
                        description = _description.value,
                        startsAt = _startsAt.value,
                        endsAt = _endsAt.value,
                        validated = _validated.value,
                    )
                )
            } ?: createEventUseCase(
                CreateEventPayload(
                    name = _name.value,
                    description = _description.value,
                    image = null,
                    startsAt = _startsAt.value,
                    endsAt = _endsAt.value,
                    validated = _validated.value,
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
