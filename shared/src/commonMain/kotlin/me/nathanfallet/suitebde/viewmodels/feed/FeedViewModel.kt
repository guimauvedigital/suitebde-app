package me.nathanfallet.suitebde.viewmodels.feed

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.usecases.associations.IFetchSubscriptionsInAssociationsUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventsUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISetupNotificationsUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

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
