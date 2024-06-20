package com.suitebde.repositories.events

import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.Instant

interface IEventsRepository {

    fun save(event: Event, expiresFromCacheAt: Instant)
    fun get(id: UUID): Event?
    fun list(pagination: Pagination): List<Event>
    fun delete(id: UUID)
    fun deleteAll()
    fun deleteExpired()

}
