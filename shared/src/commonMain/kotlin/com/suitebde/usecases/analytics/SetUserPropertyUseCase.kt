package com.suitebde.usecases.analytics

import com.suitebde.models.analytics.AnalyticsUserProperty
import com.suitebde.repositories.analytics.IAnalyticsRepository

class SetUserPropertyUseCase(
    private val analyticsRepository: IAnalyticsRepository,
) : ISetUserPropertyUseCase {

    override fun invoke(input1: AnalyticsUserProperty, input2: String?) =
        analyticsRepository.setUserProperty(input1, input2)

}
