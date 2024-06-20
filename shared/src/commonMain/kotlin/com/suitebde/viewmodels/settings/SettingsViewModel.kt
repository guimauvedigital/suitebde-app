package com.suitebde.viewmodels.settings

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.application.AlertCase
import com.suitebde.models.notifications.SubscribeToNotificationTopicType
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import com.suitebde.usecases.notifications.IGetSubscribedToNotificationTopicUseCase
import com.suitebde.usecases.notifications.ISubscribeToNotificationTopicUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
    private val getSubscribedToNotificationTopicUseCase: IGetSubscribedToNotificationTopicUseCase,
    private val subscribeToNotificationTopicUseCase: ISubscribeToNotificationTopicUseCase,
) : ViewModel() {

    // Properties

    private val _subscribedToEvents = MutableStateFlow(viewModelScope, false)

    private val _alert = MutableStateFlow<AlertCase?>(viewModelScope, null)

    @NativeCoroutinesState
    val subscribedToEvents = _subscribedToEvents.asStateFlow()

    @NativeCoroutinesState
    val alert = _alert.asStateFlow()

    // Setters

    fun setAlert(value: AlertCase?) {
        _alert.value = value
    }

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
        _subscribedToEvents.value = getSubscribedToNotificationTopicUseCase("associations_${associationId}_events")
    }

    fun subscribeToEvents(subscribe: Boolean) {
        val associationId = getAssociationIdUseCase() ?: return
        subscribeToNotificationTopicUseCase(
            "associations_${associationId}_events",
            if (subscribe) SubscribeToNotificationTopicType.SUBSCRIBE else SubscribeToNotificationTopicType.UNSUBSCRIBE
        )
    }

}
