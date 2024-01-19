package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.usecases.auth.AuthRequest
import me.nathanfallet.usecases.auth.AuthToken

class FetchTokenUseCase(
    private val environment: SuiteBDEEnvironment,
    private val client: ISuiteBDEClient,
) : IFetchTokenUseCase {

    override suspend fun invoke(input: String): AuthToken? {
        return client.auth.token(
            AuthRequest(
                "suitebde",
                "secret", // TODO: Use a real secret from env
                input
            )
        )
    }

}
