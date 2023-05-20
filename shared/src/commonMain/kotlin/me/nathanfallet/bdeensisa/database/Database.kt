package me.nathanfallet.bdeensisa.database

class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = BdeDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.bdeDatabaseQueries

    fun getCachedEvents() = dbQuery.selectCachedEvents { id, title, content, start, end ->
        CachedEvents(id, title, content, start, end)
    }.executeAsList()

    fun getCachedUserCourses() =
        dbQuery.selectCachedUserCourses { id, title, start, end, location, description ->
            CachedUserCourses(id, title, start, end, location, description)
        }.executeAsList()

    fun cacheEvents(events: List<CachedEvents>) {
        dbQuery.transaction {
            dbQuery.clearCachedEvents()
            events.forEach { event ->
                dbQuery.insertCachedEvents(
                    event.id,
                    event.title,
                    event.content,
                    event.start,
                    event.end
                )
            }
        }
    }

    fun cacheUserCourses(userCourses: List<CachedUserCourses>) {
        dbQuery.transaction {
            dbQuery.clearCachedUserCourses()
            userCourses.forEach { course ->
                dbQuery.insertCachedUserCourses(
                    course.id,
                    course.title,
                    course.start,
                    course.end,
                    course.location,
                    course.description
                )
            }
        }
    }

}