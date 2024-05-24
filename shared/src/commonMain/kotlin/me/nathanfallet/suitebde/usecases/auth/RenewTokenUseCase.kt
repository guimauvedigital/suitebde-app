package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.models.api.IAPIClient
import me.nathanfallet.ktorx.usecases.api.IRenewTokenUseCase
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.auth.RefreshTokenPayload

class RenewTokenUseCase(
    private val getRefreshTokenUseCase: IGetRefreshTokenUseCase,
    private val setTokenUseCase: ISetTokenUseCase,
) : IRenewTokenUseCase {

    override suspend fun invoke(input: IAPIClient): Boolean {
        val client = input as? ISuiteBDEClient ?: return false
        val refreshToken = getRefreshTokenUseCase() ?: return false
        val token = client.auth.refresh(RefreshTokenPayload(refreshToken)) ?: return false
        setTokenUseCase(token)
        return true
    }

}