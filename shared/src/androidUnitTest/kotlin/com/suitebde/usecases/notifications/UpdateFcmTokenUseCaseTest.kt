package com.suitebde.usecases.notifications

import com.suitebde.repositories.application.ITokenRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class UpdateFcmTokenUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val tokenRepository = mockk<ITokenRepository>()
        val setupNotificationsUseCase = mockk<ISetupNotificationsUseCase>()
        val useCase = UpdateFcmTokenUseCase(tokenRepository, setupNotificationsUseCase)
        every { tokenRepository.setFcmToken("fcmToken") } returns Unit
        coEvery { setupNotificationsUseCase() } returns Unit
        useCase("fcmToken")
        verify { tokenRepository.setFcmToken("fcmToken") }
        coVerify { setupNotificationsUseCase() }
    }

}
