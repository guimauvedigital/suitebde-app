package me.nathanfallet.suitebde.viewmodels.subscriptions

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.asStateFlow
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.usecases.associations.IFetchSubscriptionInAssociationUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase

class SubscriptionViewModel(
    private val id: String,
    private val logEventUseCase: ILogEventUseCase,
    private val fetchSubscriptionUseCase: IFetchSubscriptionInAssociationUseCase,
) : KMMViewModel() {

    // Properties

    private val _subscription = MutableStateFlow<SubscriptionInAssociation?>(viewModelScope, null)

    @NativeCoroutinesState
    val subscription = _subscription.asStateFlow()

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

}
