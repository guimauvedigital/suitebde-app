package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.BuildConfig
import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.database.IDatabaseDriverFactory
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.repositories.analytics.AnalyticsRepository
import me.nathanfallet.suitebde.repositories.analytics.IAnalyticsRepository
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.repositories.application.TokenRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val environmentModule = module {
    single {
        if (BuildConfig.FLAVOR == "dev") SuiteBDEEnvironment.DEVELOPMENT
        else SuiteBDEEnvironment.PRODUCTION
    }
    single(named("ensisa")) { BuildConfig.FLAVOR == "ensisa" }
}

val databaseModule = module {
    single<IDatabaseDriverFactory> { DatabaseDriverFactory(get()) }
}

val repositoryModule = module {
    single<ITokenRepository> { TokenRepository(get()) }
    single<IAnalyticsRepository> { AnalyticsRepository() }
}

val androidModule = listOf(
    environmentModule,
    databaseModule,
    repositoryModule
)
