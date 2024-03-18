package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class SetUserIdUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetUserIdUseCase {

    override fun invoke(input: String?) = tokenRepository.setUserId(input)

}
