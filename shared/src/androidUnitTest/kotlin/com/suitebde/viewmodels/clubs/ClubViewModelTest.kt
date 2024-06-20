package com.suitebde.viewmodels.clubs

import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.models.users.User
import com.suitebde.usecases.clubs.IListUsersInClubUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.repositories.Pagination
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test

class ClubViewModelTest {

    private val user1 = User(
        id = "user1",
        associationId = "associationId",
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false,
        Clock.System.now()
    )

    private val user2 = User(
        id = "user2",
        associationId = "associationId",
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false,
        Clock.System.now()
    )

    private val userInClub1 = UserInClub(
        userId = "user1",
        clubId = "clubId",
        roleId = "roleId",
        user = user1,
        club = null,
        role = RoleInClub(
            id = "roleId",
            clubId = "clubId",
            name = "name",
            admin = false,
            default = true
        )
    )

    private val userInClub2 = UserInClub(
        userId = "user2",
        clubId = "clubId",
        roleId = "roleId",
        user = user2,
        club = null,
        role = RoleInClub(
            id = "roleId",
            clubId = "clubId",
            name = "name",
            admin = false,
            default = true
        )
    )

    @Test
    fun testFetchUsers() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel = ClubViewModel("id", mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())

        coEvery { listUsersInClubUseCase(Pagination(25, 0), true, "id") } returns listOf(userInClub1)

        clubViewModel.fetchUsers(true)

        assertEquals(listOf(userInClub1), clubViewModel.users.value)
    }

    @Test
    fun testFetchUsersLoadMore() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel = ClubViewModel("id", mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())

        coEvery { listUsersInClubUseCase(Pagination(25, 0), false, "id") } returns listOf(userInClub1)

        clubViewModel.fetchUsers()

        coEvery { listUsersInClubUseCase(Pagination(25, 1), false, "id") } returns listOf(userInClub2)

        clubViewModel.fetchUsers()

        assertEquals(listOf(userInClub1, userInClub2), clubViewModel.users.value)
    }

    @Test
    fun testFetchUsersNotFound() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel = ClubViewModel("id", mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())

        coEvery {
            listUsersInClubUseCase(Pagination(25, 0), true, "id")
        } throws APIException(HttpStatusCode.NotFound, "users_not_found")

        clubViewModel.fetchUsers(true)

        assertEquals("users_not_found", clubViewModel.error.value)
    }
}
