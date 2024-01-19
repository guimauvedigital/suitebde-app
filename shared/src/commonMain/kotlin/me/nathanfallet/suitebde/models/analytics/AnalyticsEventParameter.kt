package me.nathanfallet.suitebde.models.analytics

import me.nathanfallet.usecases.analytics.IAnalyticsEventParameter

enum class AnalyticsEventParameter(val value: String) : IAnalyticsEventParameter {

    SCREEN_NAME("screen_name"),
    SCREEN_CLASS("screen_class"),

}
