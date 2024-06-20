package com.suitebde.usecases.notifications

import com.suitebde.repositories.application.ITokenRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFcmTokenUseCaseTest {

    @Test
    fun invoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = GetFcmTokenUseCase(tokenRepository)
        every { tokenRepository.getFcmToken() }.returns("fcmToken")
        assertEquals("fcmToken", useCase())
    }

}
