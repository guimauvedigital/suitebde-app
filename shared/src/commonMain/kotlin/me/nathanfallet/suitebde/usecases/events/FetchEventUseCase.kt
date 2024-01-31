package me.nathanfallet.suitebde.usecases.events

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchEventUseCase(
    private val client: ISuiteBDEClient,
    private val eventsRepository: IEventsRepository,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventUseCase {

    override suspend fun invoke(input1: String, input2: Boolean): Event? {
        val associationId = getAssociationIdUseCase() ?: return null
        return eventsRepository.get(input1)?.takeIf { !input2 }
            ?: client.events.get(input1, associationId)?.also {
                eventsRepository.save(
                    it,
                    Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                )
            }
    }

}
