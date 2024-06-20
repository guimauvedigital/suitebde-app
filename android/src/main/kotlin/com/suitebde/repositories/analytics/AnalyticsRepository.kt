package com.suitebde.repositories.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.models.analytics.AnalyticsUserProperty

class AnalyticsRepository : IAnalyticsRepository {

    override fun logEvent(name: AnalyticsEventName, params: Map<AnalyticsEventParameter, String>) =
        Firebase.analytics.logEvent(name.value) {
            params.forEach { (key, value) ->
                param(key.value, value)
            }
        }

    override fun setUserProperty(name: AnalyticsUserProperty, value: String?) =
        Firebase.analytics.setUserProperty(name.value, value)

}
