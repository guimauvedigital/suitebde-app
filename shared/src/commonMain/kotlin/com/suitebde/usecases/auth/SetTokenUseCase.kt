package com.suitebde.usecases.auth

import com.suitebde.models.auth.AuthToken
import com.suitebde.repositories.application.ITokenRepository

class SetTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : ISetTokenUseCase {

    override fun invoke(input: AuthToken?) {
        tokenRepository.setToken(input?.accessToken)
        tokenRepository.setRefreshToken(input?.refreshToken)
    }

}
