package me.nathanfallet.suitebde.di

import org.koin.dsl.module

val repositoryModule = module {

}

val useCaseModule = module {

}

val viewModelModule = module {

}

val sharedModule = listOf(
    repositoryModule,
    useCaseModule,
    viewModelModule,
)
