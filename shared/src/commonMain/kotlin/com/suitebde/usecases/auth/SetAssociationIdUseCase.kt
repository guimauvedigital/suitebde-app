package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.models.UUID

class SetAssociationIdUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetAssociationIdUseCase {

    override fun invoke(input: UUID?) = tokenRepository.setAssociationId(input)

}
