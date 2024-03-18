package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class SetTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetTokenUseCase {

    override fun invoke(input: String?) = tokenRepository.setToken(input)

}
