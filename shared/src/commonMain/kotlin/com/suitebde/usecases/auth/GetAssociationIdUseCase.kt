package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository

class GetAssociationIdUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetAssociationIdUseCase {

    override fun invoke() = tokenRepository.getAssociationId()

}
