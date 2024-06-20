package com.suitebde.models.analytics

import dev.kaccelero.commons.analytics.IAnalyticsEventParameter

enum class AnalyticsEventParameter(val value: String) : IAnalyticsEventParameter {

    SCREEN_NAME("screen_name"),
    SCREEN_CLASS("screen_class"),

}
