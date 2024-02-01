package me.nathanfallet.suitebde.database

import app.cash.sqldelight.db.SqlDriver

interface IDatabaseDriverFactory {

    fun createDriver(): SqlDriver

}
