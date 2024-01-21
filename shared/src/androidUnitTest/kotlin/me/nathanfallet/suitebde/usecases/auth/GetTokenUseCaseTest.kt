package me.nathanfallet.suitebde.usecases.auth

import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTokenUseCaseTest {

    @Test
    fun invoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = GetTokenUseCase(tokenRepository)
        every { tokenRepository.getToken() }.returns("token")
        assertEquals("token", useCase())
    }

}
