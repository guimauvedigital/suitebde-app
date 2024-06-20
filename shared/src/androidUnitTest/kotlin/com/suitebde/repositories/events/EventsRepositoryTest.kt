package com.suitebde.repositories.events

import com.suitebde.database.Database
import com.suitebde.database.DatabaseDriverFactory
import com.suitebde.models.events.Event
import dev.kaccelero.repositories.Pagination
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EventsRepositoryTest {

    @Test
    fun testSave() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        val event = Event(
            "id", "associationId", "name", "description", "icon",
            Clock.System.now(), Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            true
        )
        eventsRepository.save(
            event,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        assertEquals(event, eventsRepository.get("id"))
    }

    @Test
    fun testGetNotExists() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        assertEquals(null, eventsRepository.get("id"))
    }

    @Test
    fun testList() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        val event = Event(
            "id", "associationId", "name", "description", "icon",
            Clock.System.now(), Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            true
        )
        eventsRepository.save(
            event,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        assertEquals(listOf(event), eventsRepository.list(Pagination(10, 0)))
    }

    @Test
    fun testListEmpty() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        assertEquals(emptyList(), eventsRepository.list(Pagination(10, 0)))
    }

    @Test
    fun testDelete() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        val event = Event(
            "id", "associationId", "name", "description", "icon",
            Clock.System.now(), Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            true
        )
        eventsRepository.save(
            event,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        assertEquals(event, eventsRepository.get("id"))
        eventsRepository.delete("id")
        assertEquals(null, eventsRepository.get("id"))
    }

    @Test
    fun testDeleteAll() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        val event = Event(
            "id", "associationId", "name", "description", "icon",
            Clock.System.now(), Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            true
        )
        eventsRepository.save(
            event,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        assertEquals(event, eventsRepository.get("id"))
        eventsRepository.deleteAll()
        assertEquals(null, eventsRepository.get("id"))
    }

    @Test
    fun testDeleteExpired() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        val event = Event(
            "id", "associationId", "name", "description", "icon",
            Clock.System.now(), Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault()),
            true
        )
        eventsRepository.save(
            event,
            Clock.System.now().minus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        eventsRepository.deleteExpired()
        assertEquals(null, eventsRepository.get("id"))
    }

}
