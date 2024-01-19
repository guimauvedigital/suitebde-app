package me.nathanfallet.suitebde.di

import me.nathanfallet.ktorx.usecases.api.IGetTokenUseCase
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.client.SuiteBDEClient
import me.nathanfallet.suitebde.services.EnsisaClient
import me.nathanfallet.suitebde.usecases.analytics.LogEventUseCase
import me.nathanfallet.suitebde.usecases.application.IRenderDateRangeUseCase
import me.nathanfallet.suitebde.usecases.application.RenderDateRangeUseCase
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.events.*
import me.nathanfallet.suitebde.usecases.users.FetchUsersUseCase
import me.nathanfallet.suitebde.usecases.users.IFetchUsersUseCase
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.users.UsersViewModel
import me.nathanfallet.usecases.analytics.ILogEventUseCase
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
    // Application
    single<IRenderDateRangeUseCase> { RenderDateRangeUseCase() }

    // Analytics
    single<ILogEventUseCase> { LogEventUseCase(get()) }

    // Auth
    single<IFetchTokenUseCase> { FetchTokenUseCase(get(), get()) }
    single<IGetTokenUseCase> { GetTokenUseCase(get()) }
    single<IGetAssociationIdUseCase> { GetAssociationIdUseCase(get()) }
    single<ISetTokenUseCase> { SetTokenUseCase(get()) }
    single<IGetUserIdUseCase> { GetUserIdUseCase(get()) }
    single<ISetUserIdUseCase> { SetUserIdUseCase(get()) }

    // Events
    single<IFetchEventsUseCase> { FetchEventsUseCase(get(), get()) }
    single<IFetchEventUseCase> { FetchEventUseCase(get(), get()) }
    single<ICreateEventUseCase> { CreateEventUseCase(get(), get()) }
    single<IUpdateEventUseCase> { UpdateEventUseCase(get(), get()) }

    // Users
    single<IFetchUsersUseCase> { FetchUsersUseCase(get(), get()) }
}

val viewModelModule = module {
    factory { FeedViewModel(get()) }
    factory { EventViewModel(it[0], get(), get(), get(), get(), get()) }
    factory { UsersViewModel(get()) }
}

val sharedModule = listOf(
    repositoryModule,
    useCaseModule,
    viewModelModule,
)
