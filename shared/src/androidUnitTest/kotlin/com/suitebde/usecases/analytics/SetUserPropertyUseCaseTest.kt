package com.suitebde.usecases.analytics

import com.suitebde.models.analytics.AnalyticsUserProperty
import com.suitebde.repositories.analytics.IAnalyticsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class SetUserPropertyUseCaseTest {

    @Test
    fun invoke() {
        val analyticsRepository = mockk<IAnalyticsRepository>()
        every { analyticsRepository.setUserProperty(AnalyticsUserProperty.CUSTOM_PREMIUM, "true") } returns Unit
        val useCase = SetUserPropertyUseCase(analyticsRepository)
        useCase(AnalyticsUserProperty.CUSTOM_PREMIUM, "true")
        verify { analyticsRepository.setUserProperty(AnalyticsUserProperty.CUSTOM_PREMIUM, "true") }
    }

}
