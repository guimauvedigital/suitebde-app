package me.nathanfallet.suitebde.di

import io.sentry.kotlin.multiplatform.Context
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryOptions

fun initializeSentry(context: Context?) {
    val configuration: (SentryOptions) -> Unit = {
        it.dsn = "https://924adee08965753ab548bec9d57b3700@o4506105040470016.ingest.sentry.io/4506106976862208"
        // Add common configuration here
    }
    if (context != null) {
        Sentry.init(context, configuration)
    } else {
        Sentry.init(configuration)
    }
}
