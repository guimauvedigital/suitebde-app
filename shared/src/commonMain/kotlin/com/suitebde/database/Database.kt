package com.suitebde.database

class Database(databaseDriverFactory: IDatabaseDriverFactory) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())

    val eventsQueries = database.eventsDatabaseQueries

}
