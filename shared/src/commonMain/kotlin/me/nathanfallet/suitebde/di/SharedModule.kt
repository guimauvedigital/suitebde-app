package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.client.SuiteBDEClient
import me.nathanfallet.suitebde.services.EnsisaClient
import me.nathanfallet.suitebde.usecases.auth.GetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.events.FetchEventsUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventsUseCase
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<ISuiteBDEClient> {
        val realClient = SuiteBDEClient(get(), get())
        if (get(named("ensisa"))) EnsisaClient(realClient)
        else realClient
    }
}

val useCaseModule = module {
    // Auth
    single<IGetAssociationIdUseCase> { GetAssociationIdUseCase() }

    // Events
    single<IFetchEventsUseCase> { FetchEventsUseCase(get(), get()) }
}

val viewModelModule = module {
    single { FeedViewModel(get()) }
}

val sharedModule = listOf(
    repositoryModule,
    useCaseModule,
    viewModelModule,
)
