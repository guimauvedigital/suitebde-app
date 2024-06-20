package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class UpdateEventUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateEventUseCase {

    override suspend fun invoke(input1: UUID, input2: UpdateEventPayload): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.events.update(input1, input2, associationId)
    }

}
