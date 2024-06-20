package com.suitebde.usecases.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class FetchSubscriptionsInUsersUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionsInUsersUseCase {

    override suspend fun invoke(input1: UUID, input2: UUID?): List<SubscriptionInUser> {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return emptyList()
        return client.subscriptionsInUsers.list(input1, associationId)
    }

}
