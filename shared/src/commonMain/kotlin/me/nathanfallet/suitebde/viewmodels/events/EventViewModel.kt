package me.nathanfallet.suitebde.viewmodels.events

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.application.AlertCase
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.usecases.application.IRenderDateRangeUseCase
import me.nathanfallet.suitebde.usecases.events.ICreateEventUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventUseCase
import me.nathanfallet.suitebde.usecases.events.IUpdateEventUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class EventViewModel(
    private val id: String?,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchEventUseCase: IFetchEventUseCase,
    private val createEventUseCase: ICreateEventUseCase,
    private val updateEventUseCase: IUpdateEventUseCase,
    private val renderDateRangeUseCase: IRenderDateRangeUseCase,
) : KMMViewModel() {

    // Properties

    private val _event = MutableStateFlow<Event?>(viewModelScope, null)

    private val _name = MutableStateFlow(viewModelScope, "")
    private val _description = MutableStateFlow(viewModelScope, "")
    private val _startsAt = MutableStateFlow(viewModelScope, Instant.DISTANT_PAST)
    private val _endsAt = MutableStateFlow(viewModelScope, Instant.DISTANT_PAST)
    private val _validated = MutableStateFlow(viewModelScope, false)

    private val _isEditing = MutableStateFlow(viewModelScope, id == null)
    private val _hasUnsavedChanges = MutableStateFlow(viewModelScope, false)
    private val _alert = MutableStateFlow<AlertCase?>(viewModelScope, null)

    @NativeCoroutinesState
    val event = _event.asStateFlow()

    @NativeCoroutinesState
    val name = _name.asStateFlow()

    @NativeCoroutinesState
    val description = _description.asStateFlow()

    @NativeCoroutinesState
    val startsAt = _startsAt.asStateFlow()

    @NativeCoroutinesState
    val endsAt = _endsAt.asStateFlow()

    @NativeCoroutinesState
    val renderedDateRange = _event.map {
        it?.let { renderDateRangeUseCase(it.startsAt, it.endsAt) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    @NativeCoroutinesState
    val validated = _validated.asStateFlow()

    @NativeCoroutinesState
    val isEditing = _isEditing.asStateFlow()

    @NativeCoroutinesState
    val alert = _alert.asStateFlow()

    val isEditable = id != null && false
    // in case id != null: viewModel.getUser().value?.hasPermission("admin.events.edit")

    // Setters

    fun updateName(value: String) {
        _name.value = value
        _hasUnsavedChanges.value = true
    }

    fun updateDescription(value: String) {
        _description.value = value
        _hasUnsavedChanges.value = true
    }

    fun updateStartsAt(value: Instant) {
        _startsAt.value = value
        _hasUnsavedChanges.value = true
    }

    fun updateEndsAt(value: Instant) {
        _endsAt.value = value
        _hasUnsavedChanges.value = true
    }

    fun updateValidated(value: Boolean) {
        _validated.value = value
        _hasUnsavedChanges.value = true
    }

    fun setAlert(alert: AlertCase?) {
        if (_alert.value == AlertCase.SAVED && alert == null) {
            _hasUnsavedChanges.value = false
        }
        _alert.value = alert
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
    suspend fun fetchEvent() {
        _event.value = id?.let { fetchEventUseCase(id) }
    }

    private fun resetChanges() {
        _name.value = event.value?.name ?: ""
        _description.value = event.value?.description ?: ""
        _startsAt.value = event.value?.startsAt ?: Instant.DISTANT_PAST
        _endsAt.value = event.value?.endsAt ?: Instant.DISTANT_PAST
        _validated.value = event.value?.validated ?: false
        _hasUnsavedChanges.value = false
    }

    fun toggleEditing() {
        if (!isEditable) return
        if (_isEditing.value && _hasUnsavedChanges.value) {
            setAlert(AlertCase.CANCELLING)
            return
        }
        _isEditing.value = !_isEditing.value
        if (_isEditing.value) {
            resetChanges()
        }
    }

    fun discardEditingFromAlert() {
        _isEditing.value = false
    }

    @NativeCoroutines
    suspend fun saveChanges() {
        val result = try {
            id?.let {
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
                    icon = null,
                    startsAt = _startsAt.value,
                    endsAt = _endsAt.value,
                    validated = _validated.value,
                )
            )
        } catch (e: APIException) {
            null // TODO: Set error message
        }
        if (result == null) {
            setAlert(AlertCase.ERROR)
            return
        }
        _event.value = result
        setAlert(AlertCase.SAVED)
    }

}
