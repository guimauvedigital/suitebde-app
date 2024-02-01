package me.nathanfallet.suitebde.repositories.events

import kotlinx.datetime.*
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.models.events.Event
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
        assertEquals(listOf(event), eventsRepository.list(10, 0))
    }

    @Test
    fun testListEmpty() {
        val database = Database(DatabaseDriverFactory())
        val eventsRepository = EventsRepository(database)
        assertEquals(emptyList(), eventsRepository.list(10, 0))
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
