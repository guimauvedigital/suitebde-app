package me.nathanfallet.suitebde.usecases.analytics

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.repositories.analytics.IAnalyticsRepository
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
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
