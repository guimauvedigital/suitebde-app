package com.suitebde.usecases.auth

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.auth.RefreshTokenPayload
import dev.kaccelero.client.IAPIClient
import dev.kaccelero.commons.auth.IRenewTokenUseCase

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
