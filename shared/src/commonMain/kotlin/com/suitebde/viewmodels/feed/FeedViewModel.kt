package com.suitebde.viewmodels.feed

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.events.Event
import com.suitebde.models.roles.Permission
import com.suitebde.usecases.associations.IFetchSubscriptionsInAssociationsUseCase
import com.suitebde.usecases.auth.IGetCurrentUserUseCase
import com.suitebde.usecases.events.IFetchEventsUseCase
import com.suitebde.usecases.notifications.ISetupNotificationsUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.repositories.Pagination
import kotlinx.coroutines.flow.asStateFlow

class FeedViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val setupNotificationsUseCase: ISetupNotificationsUseCase,
    private val getCurrentUserUseCase: IGetCurrentUserUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val fetchSubscriptionsUseCase: IFetchSubscriptionsInAssociationsUseCase,
    private val fetchEventsUseCase: IFetchEventsUseCase,
) : ViewModel() {

    // Properties

    private val _subscriptions = MutableStateFlow<List<SubscriptionInAssociation>?>(viewModelScope, null)
    private val _events = MutableStateFlow<List<Event>?>(viewModelScope, null)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    private val _sendNotificationVisible = MutableStateFlow(viewModelScope, false)
    private val _showScannerVisible = MutableStateFlow(viewModelScope, false)

    @NativeCoroutinesState
    val subscriptions = _subscriptions.asStateFlow()

    @NativeCoroutinesState
    val events = _events.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    @NativeCoroutinesState
    val sendNotificationVisible = _sendNotificationVisible.asStateFlow()

    @NativeCoroutinesState
    val showScannerVisible = _showScannerVisible.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        setupNotificationsUseCase()
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("feed"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("FeedView")
            )
        )

        fetchFeed()
    }

    @NativeCoroutines
    suspend fun fetchFeed(reset: Boolean = false) {
        // TODO: Fetch subscriptions only if not subscribed
        fetchPermissions()
        fetchSubscriptions(reset)
        fetchEvents(reset)

        // TODO: Fetch other stuff
    }

    @NativeCoroutines
    suspend fun fetchPermissions() {
        try {
            val currentUser = getCurrentUserUseCase() ?: return
            _sendNotificationVisible.value = checkPermissionUseCase(
                currentUser, Permission.NOTIFICATIONS_SEND inAssociation currentUser.associationId
            )
            _showScannerVisible.value = checkPermissionUseCase(
                currentUser, Permission.USERS_VIEW inAssociation currentUser.associationId
            )
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @NativeCoroutines
    suspend fun fetchSubscriptions(reset: Boolean = false) {
        try {
            // TODO: Pagination
            _subscriptions.value = fetchSubscriptionsUseCase(Pagination(25, 0))
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @NativeCoroutines
    suspend fun fetchEvents(reset: Boolean = false) {
        try {
            _events.value = fetchEventsUseCase(Pagination(5, 0), reset)
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: Show a beautiful error
        }
    }

}
