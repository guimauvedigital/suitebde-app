package me.nathanfallet.suitebde.viewmodels.subscriptions

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.usecases.associations.ICheckoutSubscriptionUseCase
import me.nathanfallet.suitebde.usecases.associations.IFetchSubscriptionInAssociationUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class SubscriptionViewModel(
    private val id: String,
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
