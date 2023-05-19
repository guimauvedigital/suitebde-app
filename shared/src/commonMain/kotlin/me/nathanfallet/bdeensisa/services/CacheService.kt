package me.nathanfallet.bdeensisa.services

import kotlinx.datetime.toInstant
import me.nathanfallet.bdeensisa.database.CachedEvents
import me.nathanfallet.bdeensisa.database.CachedUserCourses
import me.nathanfallet.bdeensisa.database.Database
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.models.CalendarEvent
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.models.UserCourse

class CacheService(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)
    private val api = APIService()

    @Throws(Exception::class)
    suspend fun getEvents(token: String?, reload: Boolean): List<CalendarEvent> {
        val cachedUserCourses = database.getCachedUserCourses()
        val cachedEvents = database.getCachedEvents()
        return if (cachedEvents.isNotEmpty() && cachedUserCourses.isNotEmpty() && !reload) {
            cachedUserCourses.map {
                UserCourse(
                    it.id,
                    "",
                    it.title,
                    it.start.toInstant(),
                    it.end.toInstant(),
                    it.location,
                    it.description
                )
            } + cachedEvents.map {
                Event(
                    it.id,
                    it.title,
                    it.content,
                    it.start.toInstant(),
                    it.end.toInstant(),
                    null,
                    null
                )
            }
        } else {
            val coursesFromAPI = token?.let {
                api.getUserCourses(token, limit = 1024).also { list ->
                    database.cacheUserCourses(list.map {
                        CachedUserCourses(
                            it.adeUid,
                            it.title ?: "",
                            it.start?.toString() ?: "",
                            it.end?.toString() ?: "",
                            it.location ?: "",
                            it.description ?: ""
                        )
                    })
                }
            } ?: emptyList()
            val eventsFromAPI = api.getEvents(limit = 1024).also { list ->
                database.cacheEvents(list.map {
                    CachedEvents(
                        it.id,
                        it.title ?: "",
                        it.content ?: "",
                        it.start.toString(),
                        it.end.toString()
                    )
                })
            }
            eventsFromAPI + coursesFromAPI
        }
    }

}
