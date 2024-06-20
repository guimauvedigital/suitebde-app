package com.suitebde.usecases.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.stripe.CheckoutSession
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class CheckoutSubscriptionUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ICheckoutSubscriptionUseCase {

    override suspend fun invoke(input: UUID): CheckoutSession? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.subscriptionsInAssociations.checkout(input, associationId)
    }

}
