package me.nathanfallet.suitebde.viewmodels.notifications

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.suitebde.usecases.notifications.IListNotificationTopicsUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class SendNotificationViewModel(
    private val logEventUseCase: ILogEventUseCase,
    private val listNotificationTopicsUseCase: IListNotificationTopicsUseCase,
    private val sendNotificationUseCase: ISendNotificationUseCase,
) : KMMViewModel() {

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
            _topics.value = listNotificationTopicsUseCase()
            _sent.value = true
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
        } catch (e: APIException) {
            //_error.value = e.key
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
