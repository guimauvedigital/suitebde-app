package me.nathanfallet.suitebde.services

import kotlinx.datetime.toInstant
import me.nathanfallet.suitebde.database.CachedEvents
import me.nathanfallet.suitebde.database.CachedUserCourses
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.database.IDatabaseDriverFactory
import me.nathanfallet.suitebde.models.ensisa.CalendarEvent
import me.nathanfallet.suitebde.models.ensisa.Event
import me.nathanfallet.suitebde.models.ensisa.UserCourse

class CacheService(databaseDriverFactory: IDatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)
    private val api = APIService()

    fun apiService(): APIService {
        return api
    }

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
