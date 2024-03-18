package me.nathanfallet.suitebde.usecases.analytics

import me.nathanfallet.suitebde.models.analytics.AnalyticsUserProperty
import me.nathanfallet.suitebde.repositories.analytics.IAnalyticsRepository

class SetUserPropertyUseCase(
    private val analyticsRepository: IAnalyticsRepository,
) : ISetUserPropertyUseCase {

    override fun invoke(input1: AnalyticsUserProperty, input2: String?) =
        analyticsRepository.setUserProperty(input1, input2)

}
