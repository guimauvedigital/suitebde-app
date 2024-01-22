package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class SetAssociationIdUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetAssociationIdUseCase {

    override fun invoke(input: String?) {
        tokenRepository.setAssociationId(input)
    }

}
