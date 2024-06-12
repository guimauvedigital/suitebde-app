package me.nathanfallet.suitebde.viewmodels.feed

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventName
import me.nathanfallet.suitebde.models.analytics.AnalyticsEventParameter
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IFetchSubscriptionsInAssociationsUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetCurrentUserUseCase
import me.nathanfallet.suitebde.usecases.events.IFetchEventsUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISetupNotificationsUseCase
import me.nathanfallet.usecases.analytics.AnalyticsEventValue
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import me.nathanfallet.usecases.pagination.Pagination
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class FeedViewModelTest {

    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
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
        val setupNotificationsUseCase = mockk<ISetupNotificationsUseCase>()
        val getCurrentUserUseCase = mockk<IGetCurrentUserUseCase>()
        val checkPermissionUseCase = mockk<ICheckPermissionSuspendUseCase>()
        val fetchSubscriptionsUseCase = mockk<IFetchSubscriptionsInAssociationsUseCase>()
        val fetchEventsUseCase = mockk<IFetchEventsUseCase>()
        val feedViewModel = FeedViewModel(
            logEventUseCase,
            setupNotificationsUseCase,
            getCurrentUserUseCase,
            checkPermissionUseCase,
            fetchSubscriptionsUseCase,
            fetchEventsUseCase
        )
        every { logEventUseCase(any(), any()) } returns Unit
        coEvery { setupNotificationsUseCase() } returns Unit
        coEvery { getCurrentUserUseCase() } returns user
        coEvery {
            checkPermissionUseCase(user, Permission.NOTIFICATIONS_SEND inAssociation user.associationId)
        } returns true
        coEvery {
            checkPermissionUseCase(user, Permission.USERS_VIEW inAssociation user.associationId)
        } returns true
        coEvery { fetchSubscriptionsUseCase(Pagination(25, 0)) } returns listOf(subscription)
        coEvery { fetchEventsUseCase(Pagination(5, 0), false) } returns listOf(event)
        feedViewModel.onAppear()
        assertEquals(listOf(subscription), feedViewModel.subscriptions.value)
        assertEquals(listOf(event), feedViewModel.events.value)
        assertEquals(true, feedViewModel.sendNotificationVisible.value)
        assertEquals(true, feedViewModel.showScannerVisible.value)
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
        val feedViewModel = FeedViewModel(mockk(), mockk(), mockk(), mockk(), mockk(), fetchEventsUseCase)
        coEvery { fetchEventsUseCase(Pagination(5, 0), false) } throws APIException(
            HttpStatusCode.NotFound,
            "events_not_found"
        )
        feedViewModel.fetchEvents()
        assertEquals(feedViewModel.error.value, "events_not_found")
    }

}
