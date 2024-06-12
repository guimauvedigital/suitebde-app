package me.nathanfallet.suitebde.usecases.roles

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPermissionsForUserUseCaseTest {

    private val user = User(
        "id", "associationId", "email", null,
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
