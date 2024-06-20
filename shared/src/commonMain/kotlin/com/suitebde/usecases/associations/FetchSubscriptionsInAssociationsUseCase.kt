package com.suitebde.usecases.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.repositories.Pagination

class FetchSubscriptionsInAssociationsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionsInAssociationsUseCase {

    override suspend fun invoke(input: Pagination): List<SubscriptionInAssociation> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.subscriptionsInAssociations.list(input, associationId)
    }

}
