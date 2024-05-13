package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class UpdateUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateUserUseCase {

    override suspend fun invoke(input1: String, input2: String?, input3: UpdateUserPayload): User? {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return null
        return client.users.update(input1, input3, associationId)
    }

}
