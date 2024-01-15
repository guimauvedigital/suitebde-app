package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.BuildConfig
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import org.koin.dsl.module

val environmentModule = module {
    single {
        if (BuildConfig.FLAVOR == "dev") SuiteBDEEnvironment.DEVELOPMENT
        else SuiteBDEEnvironment.PRODUCTION
    }
}

val androidModule = listOf(
    environmentModule
)
