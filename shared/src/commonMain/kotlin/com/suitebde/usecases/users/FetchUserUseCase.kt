package com.suitebde.usecases.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class FetchUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUserUseCase {

    override suspend fun invoke(input1: UUID, input2: UUID?): User? {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return null
        return client.users.get(input1, associationId)
    }

}
