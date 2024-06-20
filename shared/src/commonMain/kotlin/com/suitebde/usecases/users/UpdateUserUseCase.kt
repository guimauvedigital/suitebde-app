package com.suitebde.usecases.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class UpdateUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateUserUseCase {

    override suspend fun invoke(input1: UUID, input2: UUID?, input3: UpdateUserPayload): User? {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return null
        return client.users.update(input1, input3, associationId)
    }

}
