package com.suitebde.di

import com.suitebde.BuildConfig
import com.suitebde.database.DatabaseDriverFactory
import com.suitebde.database.IDatabaseDriverFactory
import com.suitebde.models.application.SuiteBDEEnvironment
import com.suitebde.repositories.analytics.AnalyticsRepository
import com.suitebde.repositories.analytics.IAnalyticsRepository
import com.suitebde.repositories.application.ITokenRepository
import com.suitebde.repositories.application.TokenRepository
import com.suitebde.repositories.notifications.INotificationsRepository
import com.suitebde.repositories.notifications.NotificationsRepository
import org.koin.dsl.module

val environmentModule = module {
    single {
        if (BuildConfig.FLAVOR == "dev") SuiteBDEEnvironment.DEVELOPMENT
        else SuiteBDEEnvironment.PRODUCTION
    }
}

val databaseModule = module {
    single<IDatabaseDriverFactory> { DatabaseDriverFactory(get()) }
}

val repositoryModule = module {
    single<ITokenRepository> { TokenRepository(get()) }
    single<INotificationsRepository> { NotificationsRepository(get()) }
    single<IAnalyticsRepository> { AnalyticsRepository() }
}

val androidModule = listOf(
    environmentModule,
    databaseModule,
    repositoryModule
)
