package me.nathanfallet.suitebde.repositories.events

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.pagination.Pagination

interface IEventsRepository {

    fun save(event: Event, expiresFromCacheAt: Instant)
    fun get(id: String): Event?
    fun list(pagination: Pagination): List<Event>
    fun delete(id: String)
    fun deleteAll()
    fun deleteExpired()

}
