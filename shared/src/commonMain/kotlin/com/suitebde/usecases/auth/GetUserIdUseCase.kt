package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository

class GetUserIdUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetUserIdUseCase {

    override fun invoke() = tokenRepository.getUserId()

}
