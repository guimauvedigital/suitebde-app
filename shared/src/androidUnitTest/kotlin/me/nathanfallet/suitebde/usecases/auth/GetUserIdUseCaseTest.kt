package me.nathanfallet.suitebde.usecases.auth

import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserIdUseCaseTest {

    @Test
    fun invoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = GetUserIdUseCase(tokenRepository)
        every { tokenRepository.getUserId() }.returns("userId")
        assertEquals("userId", useCase())
    }

}
