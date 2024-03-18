package me.nathanfallet.suitebde.usecases.auth

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import kotlin.test.Test

class SetTokenUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = SetTokenUseCase(tokenRepository)
        every { tokenRepository.setToken("token") } returns Unit
        useCase("token")
        verify { tokenRepository.setToken("token") }
    }

}
