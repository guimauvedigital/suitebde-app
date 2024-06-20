package com.suitebde.viewmodels.notifications

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.NotificationTopics
import com.suitebde.usecases.notifications.IListNotificationTopicsUseCase
import com.suitebde.usecases.notifications.ISendNotificationUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.exceptions.APIException
import kotlinx.coroutines.flow.asStateFlow

class SendNotificationViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val listNotificationTopicsUseCase: IListNotificationTopicsUseCase,
    private val sendNotificationUseCase: ISendNotificationUseCase,
) : ViewModel() {

    // Properties

    private val _topics = MutableStateFlow<NotificationTopics?>(viewModelScope, null)

    private val _topic = MutableStateFlow<String?>(viewModelScope, null)
    private val _title = MutableStateFlow(viewModelScope, "")
    private val _body = MutableStateFlow(viewModelScope, "")

    private val _sent = MutableStateFlow(viewModelScope, false)
    private val _error = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val topics = _topics.asStateFlow()

    @NativeCoroutinesState
    val topic = _topic.asStateFlow()

    @NativeCoroutinesState
    val title = _title.asStateFlow()

    @NativeCoroutinesState
    val body = _body.asStateFlow()

    @NativeCoroutinesState
    val sent = _sent.asStateFlow()

    @NativeCoroutinesState
    val error = _error.asStateFlow()

    // Setters

    fun updateTopic(value: String) {
        _topic.value = value
    }

    fun updateTitle(value: String) {
        _title.value = value
    }

    fun updateBody(value: String) {
        _body.value = value
    }

    fun dismiss() {
        _sent.value = false
        _error.value = null
    }

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("send_notification"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("SendNotificationView")
            )
        )

        fetchTopics()
    }

    @NativeCoroutines
    suspend fun fetchTopics() {
        try {
            _topics.value = listNotificationTopicsUseCase()?.also {
                _topic.value = it.topics.keys.firstOrNull()
            }
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @NativeCoroutines
    suspend fun send() {
        val topic = _topic.value ?: return
        val title = _title.value.takeIf { it.isNotEmpty() } ?: return
        val body = _body.value.takeIf { it.isNotEmpty() } ?: return
        try {
            sendNotificationUseCase(
                CreateNotificationPayload(
                    topic = topic,
                    title = title,
                    body = body
                )
            )
            _sent.value = true
        } catch (e: APIException) {
            _error.value = e.key
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
