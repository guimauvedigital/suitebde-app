package com.suitebde.usecases.analytics

import com.suitebde.models.analytics.AnalyticsUserProperty
import dev.kaccelero.usecases.IPairUseCase

interface ISetUserPropertyUseCase : IPairUseCase<AnalyticsUserProperty, String?, Unit>
