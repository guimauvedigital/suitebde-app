package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.usecases.api.IGetTokenUseCase
import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class GetTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetTokenUseCase {

    override fun invoke(): String? {
        return tokenRepository.getToken()
    }

}
