package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class CheckoutSubscriptionUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ICheckoutSubscriptionUseCase {

    override suspend fun invoke(input: String): CheckoutSession? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.subscriptionsInAssociations.checkout(input, associationId)
    }

}
