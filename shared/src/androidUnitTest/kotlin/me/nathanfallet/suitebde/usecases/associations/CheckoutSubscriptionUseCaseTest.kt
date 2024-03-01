package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckoutSubscriptionUseCaseTest {

    private val session = CheckoutSession("id", "url")

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CheckoutSubscriptionUseCase(client, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.subscriptionsInAssociations.checkout("id", "associationId") } returns session
        assertEquals(session, useCase("id"))
    }

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CheckoutSubscriptionUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase("id"))
    }

}
