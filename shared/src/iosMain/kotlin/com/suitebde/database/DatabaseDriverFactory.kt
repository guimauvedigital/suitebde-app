package com.suitebde.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class DatabaseDriverFactory : IDatabaseDriverFactory {

    override fun createDriver(): SqlDriver =
        NativeSqliteDriver(AppDatabase.Schema, "suitebde.db")

}
