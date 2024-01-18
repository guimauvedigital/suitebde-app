package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment

object SwiftModule {

    lateinit var environment: SuiteBDEEnvironment

    fun module() = org.koin.dsl.module {
        single { environment }
    }

}
