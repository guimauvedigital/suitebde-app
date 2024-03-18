package me.nathanfallet.suitebde.repositories.analytics

import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.analytics.AnalyticsUserProperty

interface IAnalyticsRepository {

    fun logEvent(name: AnalyticsEventName, params: Map<AnalyticsEventParameter, String>)
    fun setUserProperty(name: AnalyticsUserProperty, value: String?)

}
