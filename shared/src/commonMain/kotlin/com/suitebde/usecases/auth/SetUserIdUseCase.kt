package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.models.UUID

class SetUserIdUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetUserIdUseCase {

    override fun invoke(input: UUID?) = tokenRepository.setUserId(input)

}
