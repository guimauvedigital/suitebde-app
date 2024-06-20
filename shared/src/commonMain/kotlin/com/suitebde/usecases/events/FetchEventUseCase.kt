package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.Event
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class FetchEventUseCase(
    private val client: ISuiteBDEClient,
    private val eventsRepository: IEventsRepository,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventUseCase {

    override suspend fun invoke(input1: UUID, input2: Boolean): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        if (input2) eventsRepository.delete(input1)
        return eventsRepository.get(input1)
            ?: client.events.get(input1, associationId)?.also {
                eventsRepository.save(
                    it,
                    Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                )
            }
    }

}
