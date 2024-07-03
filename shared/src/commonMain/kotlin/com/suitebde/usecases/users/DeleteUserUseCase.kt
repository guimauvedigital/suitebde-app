package com.suitebde.usecases.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class DeleteUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IDeleteUserUseCase {

    override suspend fun invoke(input1: UUID, input2: UUID?): Boolean {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return false
        return client.users.delete(input1, associationId)
    }

}
