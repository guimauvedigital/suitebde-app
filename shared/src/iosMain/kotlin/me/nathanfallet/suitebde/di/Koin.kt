package me.nathanfallet.suitebde.di

import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.users.UsersViewModel
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

fun KoinApplication.Companion.start(): KoinApplication = startKoin {
    modules(sharedModule)
}

// MARK: - View models (we should not call any other class from iOS directly, only use cases)

val Koin.feedViewModel: FeedViewModel get() = get()

fun Koin.eventViewModel(id: String?): EventViewModel = get {
    parametersOf(id)
}

val Koin.usersViewModel: UsersViewModel get() = get()
