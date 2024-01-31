package me.nathanfallet.suitebde.di

import me.nathanfallet.ktorx.usecases.api.IGetTokenUseCase
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.client.SuiteBDEClient
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.repositories.events.EventsRepository
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.services.EnsisaClient
import me.nathanfallet.suitebde.usecases.analytics.LogEventUseCase
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.events.*
import me.nathanfallet.suitebde.usecases.users.FetchUserUseCase
import me.nathanfallet.suitebde.usecases.users.FetchUsersUseCase
import me.nathanfallet.suitebde.usecases.users.IFetchUserUseCase
import me.nathanfallet.suitebde.usecases.users.IFetchUsersUseCase
import me.nathanfallet.suitebde.viewmodels.auth.AuthViewModel
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.root.RootViewModel
import me.nathanfallet.suitebde.viewmodels.users.UsersViewModel
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseModule = module {
    single { Database(get()) }
}

val repositoryModule = module {
    // Remote client
    single<ISuiteBDEClient> {
        val realClient = SuiteBDEClient(get(), get())
        if (get(named("ensisa"))) EnsisaClient(realClient)
        else realClient
    }

    // Local cache
    single<IEventsRepository> { EventsRepository(get()) }
}

val useCaseModule = module {
    // Analytics
    single<ILogEventUseCase> { LogEventUseCase(get()) }

    // Auth
    single<IFetchTokenUseCase> { FetchTokenUseCase(get(), get()) }
    single<IGetTokenUseCase> { GetTokenUseCase(get()) }
    single<IGetAssociationIdUseCase> { GetAssociationIdUseCase(get()) }
    single<ISetAssociationIdUseCase> { SetAssociationIdUseCase(get()) }
    single<ISetTokenUseCase> { SetTokenUseCase(get()) }
    single<IGetUserIdUseCase> { GetUserIdUseCase(get()) }
    single<ISetUserIdUseCase> { SetUserIdUseCase(get()) }

    // Events
    single<IFetchEventsUseCase> { FetchEventsUseCase(get(), get(), get()) }
    single<IFetchEventUseCase> { FetchEventUseCase(get(), get(), get()) }
    single<ICreateEventUseCase> { CreateEventUseCase(get(), get()) }
    single<IUpdateEventUseCase> { UpdateEventUseCase(get(), get()) }

    // Users
    single<IFetchUsersUseCase> { FetchUsersUseCase(get(), get()) }
    single<IFetchUserUseCase> { FetchUserUseCase(get(), get()) }
}

val viewModelModule = module {
    factory { RootViewModel(get(), get(), get()) }
    factory { AuthViewModel(get(), get(), get(), get(), get(), get()) }
    factory { FeedViewModel(get(), get()) }
    factory { EventViewModel(it[0], get(), get(), get(), get()) }
    factory { UsersViewModel(get()) }
}

val sharedModule = listOf(
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    platformModule()
)
