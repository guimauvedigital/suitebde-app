package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.usecases.auth.IGetAssociationIdUseCase

class CreateEventUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ICreateEventUseCase {

    override suspend fun invoke(input: CreateEventPayload): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.events.create(input, associationId)
    }

}
