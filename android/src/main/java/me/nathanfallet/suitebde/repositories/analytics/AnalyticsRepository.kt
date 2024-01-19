package me.nathanfallet.suitebde.repositories.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter

class AnalyticsRepository : IAnalyticsRepository {

    override fun logEvent(name: AnalyticsEventName, params: Map<AnalyticsEventParameter, String>) {
        Firebase.analytics.logEvent(name.value) {
            params.forEach { (key, value) ->
                param(key.value, value)
            }
        }
    }

}
