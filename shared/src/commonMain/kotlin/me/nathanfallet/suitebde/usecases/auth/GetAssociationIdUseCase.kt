package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class GetAssociationIdUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetAssociationIdUseCase {

    override fun invoke() = tokenRepository.getAssociationId()

}
