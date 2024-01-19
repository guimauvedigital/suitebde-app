package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class CreateEventUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ICreateEventUseCase {

    override suspend fun invoke(input: CreateEventPayload): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.events.create(input, associationId)
    }

}
