package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.client.SuiteBDEClient
import org.koin.dsl.module

val repositoryModule = module {
    single<ISuiteBDEClient> { SuiteBDEClient(get(), get()) }
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
