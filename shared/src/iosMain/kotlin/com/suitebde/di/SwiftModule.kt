package com.suitebde.di

import com.suitebde.database.DatabaseDriverFactory
import com.suitebde.database.IDatabaseDriverFactory
import com.suitebde.models.application.SuiteBDEEnvironment
import com.suitebde.repositories.analytics.IAnalyticsRepository
import com.suitebde.repositories.application.ITokenRepository
import com.suitebde.repositories.notifications.INotificationsRepository

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
