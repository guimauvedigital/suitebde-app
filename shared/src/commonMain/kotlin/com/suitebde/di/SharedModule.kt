package com.suitebde.di

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.client.SuiteBDEClient
import com.suitebde.database.Database
import com.suitebde.repositories.events.EventsRepository
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.usecases.analytics.ISetUserPropertyUseCase
import com.suitebde.usecases.analytics.LogEventUseCase
import com.suitebde.usecases.analytics.SetUserPropertyUseCase
import com.suitebde.usecases.associations.*
import com.suitebde.usecases.auth.*
import com.suitebde.usecases.clubs.*
import com.suitebde.usecases.events.*
import com.suitebde.usecases.notifications.*
import com.suitebde.usecases.roles.CheckPermissionUseCase
import com.suitebde.usecases.roles.GetPermissionsForUserUseCase
import com.suitebde.usecases.roles.IGetPermissionsForUserUseCase
import com.suitebde.usecases.scans.FetchScansUseCase
import com.suitebde.usecases.scans.IFetchScansUseCase
import com.suitebde.usecases.scans.ILogScanUseCase
import com.suitebde.usecases.scans.LogScanUseCase
import com.suitebde.usecases.users.*
import com.suitebde.viewmodels.auth.AuthViewModel
import com.suitebde.viewmodels.clubs.ClubViewModel
import com.suitebde.viewmodels.clubs.ClubsViewModel
import com.suitebde.viewmodels.events.EventViewModel
import com.suitebde.viewmodels.feed.FeedViewModel
import com.suitebde.viewmodels.feed.SearchViewModel
import com.suitebde.viewmodels.notifications.SendNotificationViewModel
import com.suitebde.viewmodels.root.RootViewModel
import com.suitebde.viewmodels.scans.ScanHistoryViewModel
import com.suitebde.viewmodels.settings.SettingsViewModel
import com.suitebde.viewmodels.subscriptions.SubscriptionViewModel
import com.suitebde.viewmodels.users.QRCodeViewModel
import com.suitebde.viewmodels.users.UserViewModel
import dev.kaccelero.commons.analytics.ILogEventUseCase
import dev.kaccelero.commons.auth.IGetTokenUseCase
import dev.kaccelero.commons.auth.ILogoutUseCase
import dev.kaccelero.commons.auth.IRenewTokenUseCase
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
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
    single<IDeleteUserUseCase> { DeleteUserUseCase(get(), get()) }
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
    factory { RootViewModel(get(), get(), get(), get()) }
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
