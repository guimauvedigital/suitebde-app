package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.models.UUID
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserIdUseCaseTest {

    @Test
    fun invoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = GetUserIdUseCase(tokenRepository)
        val userId = UUID()
        every { tokenRepository.getUserId() }.returns(userId)
        assertEquals(userId, useCase())
    }

}
