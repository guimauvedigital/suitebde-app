package me.nathanfallet.suitebde.repositories.analytics

import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter

interface IAnalyticsRepository {

    fun logEvent(name: AnalyticsEventName, params: Map<AnalyticsEventParameter, String>)

}
