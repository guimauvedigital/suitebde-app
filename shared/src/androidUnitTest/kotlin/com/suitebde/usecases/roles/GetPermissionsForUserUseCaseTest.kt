package com.suitebde.usecases.roles

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPermissionsForUserUseCaseTest {

    private val user = User(
        UUID(), UUID(), "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val useCase = GetPermissionsForUserUseCase(client)
        coEvery { client.users.listPermissions(user.id, user.associationId) } returns listOf(Permission.USERS_VIEW)
        assertEquals(setOf(Permission.USERS_VIEW), useCase(user))
    }

}
