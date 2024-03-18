package me.nathanfallet.suitebde.usecases.notifications

import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
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
