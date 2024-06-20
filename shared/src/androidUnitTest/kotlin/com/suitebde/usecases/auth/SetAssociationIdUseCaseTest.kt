package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SetAssociationIdUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = SetAssociationIdUseCase(tokenRepository)
        every { tokenRepository.setAssociationId("associationId") } returns Unit
        useCase("associationId")
        verify { tokenRepository.setAssociationId("associationId") }
    }

}
