package com.suitebde.viewmodels.clubs

import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.models.users.User
import com.suitebde.usecases.clubs.IListUsersInClubUseCase
import dev.kaccelero.commons.exceptions.APIException
import dev.kaccelero.models.UUID
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
        id = UUID(),
        associationId = UUID(),
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false,
        Clock.System.now()
    )

    private val user2 = User(
        id = UUID(),
        associationId = UUID(),
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false,
        Clock.System.now()
    )

    private val userInClub1 = UserInClub(
        userId = user1.id,
        clubId = UUID(),
        roleId = UUID(),
        user = user1,
        club = null,
        role = RoleInClub(
            id = UUID(),
            clubId = UUID(),
            name = "name",
            admin = false,
            default = true
        )
    )

    private val userInClub2 = UserInClub(
        userId = user2.id,
        clubId = userInClub1.clubId,
        roleId = UUID(),
        user = user2,
        club = null,
        role = RoleInClub(
            id = UUID(),
            clubId = UUID(),
            name = "name",
            admin = false,
            default = true
        )
    )

    @Test
    fun testFetchUsers() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel =
            ClubViewModel(userInClub1.clubId, mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())
        coEvery { listUsersInClubUseCase(Pagination(25, 0), true, userInClub1.clubId) } returns listOf(userInClub1)
        clubViewModel.fetchUsers(true)
        assertEquals(listOf(userInClub1), clubViewModel.users.value)
    }

    @Test
    fun testFetchUsersLoadMore() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel =
            ClubViewModel(userInClub1.clubId, mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())
        coEvery { listUsersInClubUseCase(Pagination(25, 0), false, userInClub1.clubId) } returns listOf(userInClub1)
        clubViewModel.fetchUsers()
        coEvery { listUsersInClubUseCase(Pagination(25, 1), false, userInClub1.clubId) } returns listOf(userInClub2)
        clubViewModel.fetchUsers()
        assertEquals(listOf(userInClub1, userInClub2), clubViewModel.users.value)
    }

    @Test
    fun testFetchUsersNotFound() = runBlocking {
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val clubViewModel =
            ClubViewModel(userInClub1.clubId, mockk(), mockk(), listUsersInClubUseCase, mockk(), mockk())
        coEvery {
            listUsersInClubUseCase(Pagination(25, 0), true, userInClub1.clubId)
        } throws APIException(HttpStatusCode.NotFound, "users_not_found")
        clubViewModel.fetchUsers(true)
        assertEquals("users_not_found", clubViewModel.error.value)
    }
}
