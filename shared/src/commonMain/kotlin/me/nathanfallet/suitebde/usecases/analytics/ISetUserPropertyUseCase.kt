package me.nathanfallet.suitebde.usecases.analytics

import me.nathanfallet.suitebde.models.analytics.AnalyticsUserProperty
import me.nathanfallet.usecases.base.IPairUseCase

interface ISetUserPropertyUseCase : IPairUseCase<AnalyticsUserProperty, String?, Unit>
