package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository
import dev.kaccelero.commons.auth.IGetTokenUseCase

class GetTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetTokenUseCase {

    override fun invoke() = tokenRepository.getToken()

}
