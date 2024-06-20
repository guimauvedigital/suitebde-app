package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.models.UUID
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SetUserIdUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = SetUserIdUseCase(tokenRepository)
        val userId = UUID()
        every { tokenRepository.setUserId(userId) } returns Unit
        useCase(userId)
        verify { tokenRepository.setUserId(userId) }
    }

}
