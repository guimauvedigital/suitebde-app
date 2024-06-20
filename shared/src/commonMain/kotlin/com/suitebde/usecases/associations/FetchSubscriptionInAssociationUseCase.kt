package com.suitebde.usecases.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class FetchSubscriptionInAssociationUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchSubscriptionInAssociationUseCase {

    override suspend fun invoke(input: UUID): SubscriptionInAssociation? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.subscriptionsInAssociations.get(input, associationId)
    }

}
