package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchEventUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventUseCase {

    override suspend fun invoke(input: String): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.events.get(input, associationId)
    }

}
