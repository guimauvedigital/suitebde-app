package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class UpdateEventUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateEventUseCase {

    override suspend fun invoke(input1: String, input2: UpdateEventPayload): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.events.update(input1, input2, associationId)
    }

}
