package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class FetchSubscriptionsInUsersUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionsInUsersUseCase {

    override suspend fun invoke(input1: String, input2: String?): List<SubscriptionInUser> {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return emptyList()
        return client.subscriptionsInUsers.list(Pagination(0, 0), input1, associationId) // TODO: Remove pagination
    }

}
