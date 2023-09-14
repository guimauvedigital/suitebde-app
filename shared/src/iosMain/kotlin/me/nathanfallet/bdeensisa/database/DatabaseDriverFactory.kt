package me.nathanfallet.bdeensisa.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(BdeDatabase.Schema, "bdeensisa.db")
    }
}
