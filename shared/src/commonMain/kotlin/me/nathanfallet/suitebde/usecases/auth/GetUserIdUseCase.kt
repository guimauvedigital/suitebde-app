package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class GetUserIdUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetUserIdUseCase {

    override fun invoke() = tokenRepository.getUserId()

}
