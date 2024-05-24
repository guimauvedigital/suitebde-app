package me.nathanfallet.suitebde.di

import me.nathanfallet.ktorx.usecases.api.IGetTokenUseCase
import me.nathanfallet.ktorx.usecases.api.ILogoutUseCase
import me.nathanfallet.ktorx.usecases.api.IRenewTokenUseCase
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.client.SuiteBDEClient
import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.repositories.events.EventsRepository
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.usecases.analytics.ISetUserPropertyUseCase
import me.nathanfallet.suitebde.usecases.analytics.LogEventUseCase
import me.nathanfallet.suitebde.usecases.analytics.SetUserPropertyUseCase
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.suitebde.usecases.clubs.*
import me.nathanfallet.suitebde.usecases.events.*
import me.nathanfallet.suitebde.usecases.notifications.*
import me.nathanfallet.suitebde.usecases.roles.CheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.roles.GetPermissionsForUserUseCase
import me.nathanfallet.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import me.nathanfallet.suitebde.usecases.scans.FetchScansUseCase
import me.nathanfallet.suitebde.usecases.scans.IFetchScansUseCase
import me.nathanfallet.suitebde.usecases.scans.ILogScanUseCase
import me.nathanfallet.suitebde.usecases.scans.LogScanUseCase
import me.nathanfallet.suitebde.usecases.users.*
import me.nathanfallet.suitebde.viewmodels.auth.AuthViewModel
import me.nathanfallet.suitebde.viewmodels.clubs.ClubViewModel
import me.nathanfallet.suitebde.viewmodels.clubs.ClubsViewModel
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.feed.SearchViewModel
import me.nathanfallet.suitebde.viewmodels.notifications.SendNotificationViewModel
import me.nathanfallet.suitebde.viewmodels.root.RootViewModel
import me.nathanfallet.suitebde.viewmodels.scans.ScanHistoryViewModel
import me.nathanfallet.suitebde.viewmodels.settings.SettingsViewModel
import me.nathanfallet.suitebde.viewmodels.subscriptions.SubscriptionViewModel
import me.nathanfallet.suitebde.viewmodels.users.QRCodeViewModel
import me.nathanfallet.suitebde.viewmodels.users.UserViewModel
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import org.koin.dsl.module

val databaseModule = module {
    single { Database(get()) }
}

val repositoryModule = module {
    // Remote client
    single<ISuiteBDEClient> { SuiteBDEClient(get(), get(), get(), get()) }

    // Local cache
    single<IEventsRepository> { EventsRepository(get()) }
}

val useCaseModule = module {
    // Analytics
    single<ILogEventUseCase> { LogEventUseCase(get()) }
    single<ISetUserPropertyUseCase> { SetUserPropertyUseCase(get()) }

    // Associations
    single<IFetchSubscriptionsInAssociationsUseCase> { FetchSubscriptionsInAssociationsUseCase(get(), get()) }
    single<IFetchSubscriptionInAssociationUseCase> { FetchSubscriptionInAssociationUseCase(get(), get()) }

    // Auth
    single<IFetchTokenUseCase> { FetchTokenUseCase(get(), get(), get(), get(), get()) }
    single<IGetTokenUseCase> { GetTokenUseCase(get()) }
    single<IGetRefreshTokenUseCase> { GetRefreshTokenUseCase(get()) }
    single<IGetAssociationIdUseCase> { GetAssociationIdUseCase(get()) }
    single<ISetAssociationIdUseCase> { SetAssociationIdUseCase(get()) }
    single<ISetTokenUseCase> { SetTokenUseCase(get()) }
    single<IGetUserIdUseCase> { GetUserIdUseCase(get()) }
    single<ISetUserIdUseCase> { SetUserIdUseCase(get()) }
    single<IRenewTokenUseCase> { RenewTokenUseCase(get(), get()) }
    single<ILogoutUseCase> { LogoutUseCase(get(), get(), get(), get()) }
    single<IGetCurrentUserUseCase> { GetCurrentUserUseCase(get(), get()) }

    // Notifications
    single<IGetFcmTokenUseCase> { GetFcmTokenUseCase(get()) }
    single<IUpdateFcmTokenUseCase> { UpdateFcmTokenUseCase(get(), get()) }
    single<ISetupNotificationsUseCase> { SetupNotificationsUseCase(get(), get(), get(), get()) }
    single<ISubscribeToNotificationTopicUseCase> { SubscribeToNotificationTopicUseCase(get()) }
    single<IGetSubscribedToNotificationTopicUseCase> { GetSubscribedToNotificationTopicUseCase(get()) }
    single<IClearFcmTokenUseCase> { ClearFcmTokenUseCase(get(), get()) }
    single<ISendNotificationTokenUseCase> { SendNotificationTokenUseCase(get(), get(), get()) }
    single<IListNotificationTopicsUseCase> { ListNotificationTopicsUseCase(get(), get()) }
    single<ISendNotificationUseCase> { SendNotificationUseCase(get(), get()) }

    // Subscriptions
    single<ICheckoutSubscriptionUseCase> { CheckoutSubscriptionUseCase(get(), get()) }

    // Clubs
    single<IUpdateUserInClubUseCase> { UpdateUserInClubUseCase(get(), get(), get()) }
    single<IFetchClubsUseCase> { FetchClubsUseCase(get(), get()) }
    single<IFetchClubUseCase> { FetchClubUseCase(get(), get()) }
    single<IListUsersInClubUseCase> { ListUsersInClubUseCase(get(), get()) }

    // Events
    single<IFetchEventsUseCase> { FetchEventsUseCase(get(), get(), get()) }
    single<IFetchEventUseCase> { FetchEventUseCase(get(), get(), get()) }
    single<ICreateEventUseCase> { CreateEventUseCase(get(), get()) }
    single<IUpdateEventUseCase> { UpdateEventUseCase(get(), get()) }

    // Users
    single<IFetchUsersUseCase> { FetchUsersUseCase(get(), get()) }
    single<IFetchUserUseCase> { FetchUserUseCase(get(), get()) }
    single<IUpdateUserUseCase> { UpdateUserUseCase(get(), get()) }
    single<IFetchSubscriptionsInUsersUseCase> { FetchSubscriptionsInUsersUseCase(get(), get()) }

    // Scans
    single<ILogScanUseCase> { LogScanUseCase(get(), get()) }
    single<IFetchScansUseCase> { FetchScansUseCase(get(), get()) }

    // Roles and permissions
    single<IGetPermissionsForUserUseCase> { GetPermissionsForUserUseCase(get()) }
    single<ICheckPermissionSuspendUseCase> { CheckPermissionUseCase(get()) }
}

val viewModelModule = module {
    // Root and auth
    factory { RootViewModel(get(), get(), get()) }
    factory { AuthViewModel(get(), get(), get()) }
    factory { SettingsViewModel(get(), get(), get(), get()) }

    // Feed
    factory { FeedViewModel(get(), get(), get(), get(), get(), get()) }
    factory { SearchViewModel(get(), get()) }
    factory { EventViewModel(it[0], get(), get(), get(), get(), get(), get()) }
    factory { SubscriptionViewModel(it[0], get(), get(), get()) }
    factory { SendNotificationViewModel(get(), get(), get()) }

    // Clubs
    factory { ClubsViewModel(get(), get()) }
    factory { ClubViewModel(it[0], get(), get(), get(), get(), get()) }

    // Users
    factory { QRCodeViewModel(get()) }
    factory { UserViewModel(it[0], it[1], get(), get(), get(), get(), get(), get()) }

    // Scans
    factory { ScanHistoryViewModel(get(), get()) }
}

val sharedModule = listOf(
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    platformModule()
)
