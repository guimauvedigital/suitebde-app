package me.nathanfallet.suitebde.repositories.events

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.Events
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.pagination.Pagination

class EventsRepository(
    private val database: Database,
) : IEventsRepository {

    private fun Events.toEvent() =
        Event(
            id = id,
            associationId = associationId,
            name = name,
            description = description,
            image = image,
            startsAt = startsAt.let(Instant::parse),
            endsAt = endsAt.let(Instant::parse),
            validated = validated == 1L
        )

    override fun save(event: Event, expiresFromCacheAt: Instant) =
        database.eventsQueries.save(
            Events(
                id = event.id,
                associationId = event.associationId,
                name = event.name,
                description = event.description,
                image = event.image,
                startsAt = event.startsAt.toString(),
                endsAt = event.endsAt.toString(),
                validated = if (event.validated) 1 else 0,
                expiresFromCacheAt = expiresFromCacheAt.toString()
            )
        )

    override fun get(id: String): Event? =
        database.eventsQueries.get(id, Clock.System.now().toString())
            .executeAsOneOrNull()?.toEvent()


    override fun list(pagination: Pagination): List<Event> =
        database.eventsQueries.list(Clock.System.now().toString(), pagination.limit, pagination.offset)
            .executeAsList().map { it.toEvent() }

    override fun delete(id: String) =
        database.eventsQueries.delete(id)

    override fun deleteAll() =
        database.eventsQueries.deleteAll()

    override fun deleteExpired() =
        database.eventsQueries.deleteExpired(Clock.System.now().toString())

}
