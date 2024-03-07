package me.nathanfallet.suitebde.usecases.events

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class FetchEventsUseCase(
    private val client: ISuiteBDEClient,
    private val eventsRepository: IEventsRepository,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventsUseCase {

    override suspend fun invoke(input1: Pagination, input2: Boolean): List<Event> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        if (input2) eventsRepository.deleteAll()
        else eventsRepository.deleteExpired()
        return eventsRepository.list(input1).takeIf { it.isNotEmpty() }
            ?: client.events.list(input1, associationId).onEach {
                eventsRepository.save(
                    it,
                    Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                )
            }
    }

}
