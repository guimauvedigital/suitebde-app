package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.database.IDatabaseDriverFactory
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.repositories.analytics.IAnalyticsRepository
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.repositories.notifications.INotificationsRepository

object SwiftModule {

    lateinit var environment: SuiteBDEEnvironment
    lateinit var tokenRepository: ITokenRepository
    lateinit var notificationsRepository: INotificationsRepository
    lateinit var analyticsRepository: IAnalyticsRepository

    fun module() = org.koin.dsl.module {
        single<IDatabaseDriverFactory> { DatabaseDriverFactory() }

        single { environment }
        single { tokenRepository }
        single { notificationsRepository }
        single { analyticsRepository }
    }

}
