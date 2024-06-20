package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.models.UUID
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationIdUseCaseTest {

    @Test
    fun invoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val useCase = GetAssociationIdUseCase(tokenRepository)
        val associationId = UUID()
        every { tokenRepository.getAssociationId() }.returns(associationId)
        assertEquals(associationId, useCase())
    }

}
