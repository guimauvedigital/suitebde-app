package com.suitebde.repositories.events

import com.suitebde.database.Database
import com.suitebde.database.Events
import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class EventsRepository(
    private val database: Database,
) : IEventsRepository {

    private fun Events.toEvent() =
        Event(
            id = UUID(id),
            associationId = UUID(associationId),
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
                id = event.id.toString(),
                associationId = event.associationId.toString(),
                name = event.name,
                description = event.description,
                image = event.image,
                startsAt = event.startsAt.toString(),
                endsAt = event.endsAt.toString(),
                validated = if (event.validated) 1 else 0,
                expiresFromCacheAt = expiresFromCacheAt.toString()
            )
        )

    override fun get(id: UUID): Event? =
        database.eventsQueries.get(id.toString(), Clock.System.now().toString())
            .executeAsOneOrNull()?.toEvent()


    override fun list(pagination: Pagination): List<Event> =
        database.eventsQueries.list(Clock.System.now().toString(), pagination.limit, pagination.offset)
            .executeAsList().map { it.toEvent() }

    override fun delete(id: UUID) =
        database.eventsQueries.delete(id.toString())

    override fun deleteAll() =
        database.eventsQueries.deleteAll()

    override fun deleteExpired() =
        database.eventsQueries.deleteExpired(Clock.System.now().toString())

}
