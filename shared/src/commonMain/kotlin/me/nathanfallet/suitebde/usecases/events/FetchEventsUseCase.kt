package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchEventsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventsUseCase {

    override suspend fun invoke(input1: Long, input2: Long): List<Event> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.events.list(input1, input2, associationId)
    }

}
