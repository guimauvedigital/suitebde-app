package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.viewmodels.auth.AuthViewModel
import me.nathanfallet.suitebde.viewmodels.clubs.ClubViewModel
import me.nathanfallet.suitebde.viewmodels.clubs.ClubsViewModel
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.feed.SearchViewModel
import me.nathanfallet.suitebde.viewmodels.root.RootViewModel
import me.nathanfallet.suitebde.viewmodels.settings.SettingsViewModel
import me.nathanfallet.suitebde.viewmodels.subscriptions.SubscriptionViewModel
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

fun Koin.eventViewModel(id: String?): EventViewModel = get {
    parametersOf(id)
}

fun Koin.subscriptionViewModel(id: String): SubscriptionViewModel = get {
    parametersOf(id)
}

val Koin.clubsViewModel: ClubsViewModel get() = get()

fun Koin.clubViewModel(id: String?): ClubViewModel = get {
    parametersOf(id)
}
