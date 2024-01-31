package me.nathanfallet.suitebde.repositories.events

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.events.Event

interface IEventsRepository {

    fun save(event: Event, expiresFromCacheAt: Instant)
    fun get(id: String): Event?
    fun list(limit: Long, offset: Long): List<Event>
    fun delete(id: String)
    fun deleteAll()
    fun deleteExpired()

}
