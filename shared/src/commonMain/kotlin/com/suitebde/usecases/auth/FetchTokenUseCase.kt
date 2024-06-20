package com.suitebde.usecases.auth

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.application.SuiteBDEEnvironment
import com.suitebde.models.auth.AuthRequest
import com.suitebde.models.auth.AuthToken
import dev.kaccelero.models.UUID

class FetchTokenUseCase(
    private val environment: SuiteBDEEnvironment,
    private val client: ISuiteBDEClient,
    private val setTokenUseCase: ISetTokenUseCase,
    private val setUserIdUseCase: ISetUserIdUseCase,
    private val setAssociationIdUseCase: ISetAssociationIdUseCase,
) : IFetchTokenUseCase {

    override suspend fun invoke(input: String): AuthToken? {
        return client.auth.token(
            AuthRequest(
                UUID("00000000-0000-0000-0000-000000000000"),
                "camR4VrfHyWCBqMexS3UvdpbLX2DsuGk",
                input
            )
        )?.also {
            val (associationId, userId) = it.idToken.split("/").map(::UUID)
            setTokenUseCase(it)
            setAssociationIdUseCase(associationId)
            setUserIdUseCase(userId)
        }
    }

}
