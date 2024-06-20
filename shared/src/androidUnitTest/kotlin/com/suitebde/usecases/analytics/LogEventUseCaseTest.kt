package com.suitebde.usecases.analytics

import com.suitebde.models.analytics.AnalyticsEventName
import com.suitebde.models.analytics.AnalyticsEventParameter
import com.suitebde.repositories.analytics.IAnalyticsRepository
import dev.kaccelero.commons.analytics.AnalyticsEventValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class LogEventUseCaseTest {

    @Test
    fun invoke() {
        val analyticsRepository = mockk<IAnalyticsRepository>()
        val useCase = LogEventUseCase(analyticsRepository)
        every { analyticsRepository.logEvent(any(), any()) } returns Unit
        useCase(
            AnalyticsEventName.SCREEN_VIEW, mapOf(
                AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("screen"),
                AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("ScreenView"),
            )
        )
        verify {
            analyticsRepository.logEvent(
                AnalyticsEventName.SCREEN_VIEW, mapOf(
                    AnalyticsEventParameter.SCREEN_NAME to "screen",
                    AnalyticsEventParameter.SCREEN_CLASS to "ScreenView",
                )
            )
        }
    }

}
