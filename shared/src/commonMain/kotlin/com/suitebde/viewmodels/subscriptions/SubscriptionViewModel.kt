package com.suitebde.viewmodels.subscriptions

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.usecases.associations.ICheckoutSubscriptionUseCase
import com.suitebde.usecases.associations.IFetchSubscriptionInAssociationUseCase
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.models.UUID
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel(
    private val id: UUID,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchSubscriptionUseCase: IFetchSubscriptionInAssociationUseCase,
    private val checkoutSubscriptionUseCase: ICheckoutSubscriptionUseCase,
) : ViewModel() {

    // Properties

    private val _subscription = MutableStateFlow<SubscriptionInAssociation?>(viewModelScope, null)
    private val _url = MutableStateFlow<String?>(viewModelScope, null)

    @NativeCoroutinesState
    val subscription = _subscription.asStateFlow()

    @NativeCoroutinesState
    val url = _url.asStateFlow()

    // Methods

    @NativeCoroutines
    suspend fun onAppear() {
        logEventUseCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("subscription"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("SubscriptionView")
            )
        )

        fetchSubscription()
    }

    @NativeCoroutines
    suspend fun fetchSubscription(reset: Boolean = false) {
        try {
            _subscription.value = fetchSubscriptionUseCase(id)
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: Show a beautiful error
        }
    }

    @NativeCoroutines
    suspend fun checkoutSubscription() {
        try {
            _url.value = checkoutSubscriptionUseCase(id)?.url
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: Show a beautiful error
        }
    }

    fun close() {
        _url.value = null
    }

}
