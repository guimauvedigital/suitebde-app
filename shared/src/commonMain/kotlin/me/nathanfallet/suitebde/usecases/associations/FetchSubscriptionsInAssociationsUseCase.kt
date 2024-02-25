package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchSubscriptionsInAssociationsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionsInAssociationsUseCase {

    override suspend fun invoke(input1: Long, input2: Long): List<SubscriptionInAssociation> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.subscriptionsInAssociations.list(input1, input2, associationId)
    }

}
