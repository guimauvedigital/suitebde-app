package me.nathanfallet.suitebde.viewmodels.feed

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.associations.IFetchSubscriptionsInAssociationsUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventsUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedViewModelTest {

    private val subscription = SubscriptionInAssociation(
        "id", "associationId", "name", "description",
        85.0, "1d", false
    )
    private val event = Event(
        "id", "associationId", "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testOnAppear() = runBlocking {
        val logEventUseCase = mockk<ILogEventUseCase>()
        val fetchSubscriptionsUseCase = mockk<IFetchSubscriptionsInAssociationsUseCase>()
        val fetchEventsUseCase = mockk<IFetchEventsUseCase>()
        val feedViewModel = FeedViewModel(logEventUseCase, fetchSubscriptionsUseCase, fetchEventsUseCase)
        every { logEventUseCase(any(), any()) } returns Unit
        coEvery { fetchSubscriptionsUseCase.invoke(25, 0) } returns listOf(subscription)
        coEvery { fetchEventsUseCase.invoke(5, 0, false) } returns listOf(event)
        feedViewModel.onAppear()
        assertEquals(feedViewModel.subscriptions.value, listOf(subscription))
        assertEquals(feedViewModel.events.value, listOf(event))
        verify {
            logEventUseCase(
                AnalyticsEventName.SCREEN_VIEW, mapOf(
                    AnalyticsEventParameter.SCREEN_NAME to AnalyticsEventValue("feed"),
                    AnalyticsEventParameter.SCREEN_CLASS to AnalyticsEventValue("FeedView")
                )
            )
        }
    }

    @Test
    fun testLoadEventsWithError() = runBlocking {
        val fetchEventsUseCase = mockk<IFetchEventsUseCase>()
        val feedViewModel = FeedViewModel(mockk(), mockk(), fetchEventsUseCase)
        coEvery { fetchEventsUseCase.invoke(5, 0, false) } throws APIException(
            HttpStatusCode.NotFound,
            "events_not_found"
        )
        feedViewModel.fetchEvents()
        assertEquals(feedViewModel.error.value, "events_not_found")
    }

}
