package me.nathanfallet.suitebde.models.analytics

import me.nathanfallet.usecases.analytics.IAnalyticsEventName

enum class AnalyticsEventName(val value: String) : IAnalyticsEventName {

    SCREEN_VIEW("screen_view"),

}
