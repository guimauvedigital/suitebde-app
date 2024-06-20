package com.suitebde.usecases.associations

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.stripe.CheckoutSession
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckoutSubscriptionUseCaseTest {

    private val session = CheckoutSession("id", "url")

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CheckoutSubscriptionUseCase(client, getAssociationIdUseCase)
        val id = UUID()
        val associationId = UUID()
        every { getAssociationIdUseCase() } returns associationId
        coEvery { client.subscriptionsInAssociations.checkout(id, associationId) } returns session
        assertEquals(session, useCase(id))
    }

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CheckoutSubscriptionUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase(UUID()))
    }

}
