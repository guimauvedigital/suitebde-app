package com.suitebde.di

import com.suitebde.usecases.notifications.IUpdateFcmTokenUseCase
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
import dev.kaccelero.models.UUID
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

fun KoinApplication.Companion.start(): KoinApplication = startKoin {
    modules(sharedModule)
}

// MARK: - View models (we should not call any other class from iOS directly, only view models)

val Koin.rootViewModel: RootViewModel get() = get()
val Koin.authViewModel: AuthViewModel get() = get()
val Koin.settingsViewModel: SettingsViewModel get() = get()

val Koin.feedViewModel: FeedViewModel get() = get()
val Koin.searchViewModel: SearchViewModel get() = get()
val Koin.sendNotificationViewModel: SendNotificationViewModel get() = get()
val Koin.scanHistoryViewModel: ScanHistoryViewModel get() = get()

fun Koin.eventViewModel(id: UUID?): EventViewModel = get {
    parametersOf(id)
}

fun Koin.subscriptionViewModel(id: UUID): SubscriptionViewModel = get {
    parametersOf(id)
}

val Koin.clubsViewModel: ClubsViewModel get() = get()

fun Koin.clubViewModel(id: UUID?): ClubViewModel = get {
    parametersOf(id)
}

val Koin.qrCodeViewModel: QRCodeViewModel get() = get()

fun Koin.userViewModel(associationId: UUID, userId: UUID): UserViewModel = get {
    parametersOf(associationId, userId)
}

// MARK: - Use cases

// This is the only use case we need to call from iOS, as we need to call it when we receive a new token
val Koin.updateFcmTokenUseCase: IUpdateFcmTokenUseCase get() = get()
