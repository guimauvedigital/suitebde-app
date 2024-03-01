package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchSubscriptionInAssociationUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionInAssociationUseCase {

    override suspend fun invoke(input: String): SubscriptionInAssociation? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.subscriptionsInAssociations.get(input, associationId)
    }

}
