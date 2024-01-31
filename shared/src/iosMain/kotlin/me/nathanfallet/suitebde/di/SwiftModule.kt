package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.repositories.analytics.IAnalyticsRepository
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import org.koin.core.qualifier.named
import kotlin.properties.Delegates

object SwiftModule {

    var ensisa by Delegates.notNull<Boolean>()

    lateinit var environment: SuiteBDEEnvironment
    lateinit var tokenRepository: ITokenRepository
    lateinit var analyticsRepository: IAnalyticsRepository

    fun module() = org.koin.dsl.module {
        single { DatabaseDriverFactory() }

        single(named("ensisa")) { ensisa }
        single { environment }
        single { tokenRepository }
        single { analyticsRepository }
    }

}
