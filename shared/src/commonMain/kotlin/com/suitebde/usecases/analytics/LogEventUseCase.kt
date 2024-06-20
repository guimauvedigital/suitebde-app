package com.suitebde.usecases.analytics

import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.repositories.analytics.IAnalyticsRepository
import dev.kaccelero.commons.analytics.*

class LogEventUseCase(
    private val analyticsRepository: IAnalyticsRepository,
) : ILogEventUseCase {

    override fun invoke(input1: IAnalyticsEventName, input2: Map<IAnalyticsEventParameter, IAnalyticsEventValue>) {
        val name = input1 as? AnalyticsEventName ?: return
        val parameters = input2.entries.associate {
            (it.key as? AnalyticsEventParameter ?: return) to
                    ((it.value as? AnalyticsEventValue<*> ?: return).value as? String ?: return)
        }
        analyticsRepository.logEvent(name, parameters)
    }

}
