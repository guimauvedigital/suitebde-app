package com.suitebde.repositories.analytics

import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.analytics.AnalyticsUserProperty

interface IAnalyticsRepository {

    fun logEvent(name: AnalyticsEventName, params: Map<AnalyticsEventParameter, String>)
    fun setUserProperty(name: AnalyticsUserProperty, value: String?)

}
