package me.nathanfallet.suitebde.viewmodels.settings

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.suitebde.models.SubscribeToNotificationTopicType
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.notifications.IGetSubscribedToNotificationTopicUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISubscribeToNotificationTopicUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class SettingsViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
    private val getSubscribedToNotificationTopicUseCase: IGetSubscribedToNotificationTopicUseCase,
    private val subscribeToNotificationTopicUseCase: ISubscribeToNotificationTopicUseCase,
) : KMMViewModel() {

    // Properties

    private val _subscribedToEvents = MutableStateFlow(viewModelScope, false)

    @NativeCoroutinesState
    val subscribedToEvents = _subscribedToEvents.asStateFlow()

    // Methods

    fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("settings"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("SettingsView")
            )
        )

        loadSubscriptions()
    }

    private fun loadSubscriptions() {
        val associationId = getAssociationIdUseCase() ?: return
        _subscribedToEvents.value = getSubscribedToNotificationTopicUseCase("associations/$associationId/events")
    }

    fun subscribeToEvents(subscribe: Boolean) {
        val associationId = getAssociationIdUseCase() ?: return
        subscribeToNotificationTopicUseCase(
            "associations/$associationId/events",
            if (subscribe) SubscribeToNotificationTopicType.SUBSCRIBE else SubscribeToNotificationTopicType.UNSUBSCRIBE
        )
    }

}
