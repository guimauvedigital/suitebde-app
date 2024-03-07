package me.nathanfallet.suitebde.di

import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryOptions

fun initializeSentry() {
    val configuration: (SentryOptions) -> Unit = {
        it.dsn = "https://924adee08965753ab548bec9d57b3700@o4506105040470016.ingest.sentry.io/4506106976862208"
        // Add common configuration here
    }
    Sentry.init(configuration)
}
