package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class GetRefreshTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetRefreshTokenUseCase {

    override fun invoke() = tokenRepository.getRefreshToken()

}