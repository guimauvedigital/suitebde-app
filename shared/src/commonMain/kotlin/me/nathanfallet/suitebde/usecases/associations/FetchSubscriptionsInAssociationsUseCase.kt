package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class FetchSubscriptionsInAssociationsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionsInAssociationsUseCase {

    override suspend fun invoke(input: Pagination): List<SubscriptionInAssociation> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.subscriptionsInAssociations.list(input, associationId)
    }

}
