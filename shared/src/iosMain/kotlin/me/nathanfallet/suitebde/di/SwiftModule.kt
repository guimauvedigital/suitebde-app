package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.repositories.application.ITokenRepository

object SwiftModule {

    lateinit var environment: SuiteBDEEnvironment
    lateinit var tokenRepository: ITokenRepository

    fun module() = org.koin.dsl.module {
        single { environment }
        single { tokenRepository }
    }

}
