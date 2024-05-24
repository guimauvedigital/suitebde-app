package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.usecases.auth.AuthToken

class SetTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetTokenUseCase {

    override fun invoke(input: AuthToken?) {
        tokenRepository.setToken(input?.accessToken)
        tokenRepository.setRefreshToken(input?.refreshToken)
    }

}
