package me.nathanfallet.suitebde.usecases.events

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchEventsUseCase(
    private val client: ISuiteBDEClient,
    private val eventsRepository: IEventsRepository,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchEventsUseCase {

    override suspend fun invoke(input1: Long, input2: Long, input3: Boolean): List<Event> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return eventsRepository.list(input1, input2).takeIf { it.isNotEmpty() && !input3 }
            ?: client.events.list(input1, input2, associationId).also {
                eventsRepository.deleteExpired()
                it.forEach { event ->
                    eventsRepository.save(
                        event,
                        Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
                    )
                }
            }
    }

}
