package me.nathanfallet.suitebde.viewmodels.clubs

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import me.nathanfallet.ktorx.models.exceptions.APIException
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.clubs.IFetchClubUseCase
import me.nathanfallet.suitebde.usecases.clubs.IListUsersInClubUseCase
import me.nathanfallet.suitebde.usecases.clubs.IUpdateUserInClubUseCase
import me.nathanfallet.usecases.analytics.ILogEventUseCase
import kotlin.test.Test

class ClubViewModelTest {
    private val user1 = User(
        id = "user1",
        associationId = "associationId",
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false
    )

    private val user2 = User(
        id = "user2",
        associationId = "associationId",
        email = "email",
        password = "password",
        firstName = "firstname",
        lastName = "lastname",
        superuser = false
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
        val logEventUseCase = mockk<ILogEventUseCase>()
        val fetchClubUseCase = mockk<IFetchClubUseCase>()
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val updateUserInClubUseCase = mockk<IUpdateUserInClubUseCase>()
        val clubViewModel =
            ClubViewModel("id", logEventUseCase, fetchClubUseCase, listUsersInClubUseCase, updateUserInClubUseCase)

        coEvery {
            listUsersInClubUseCase(25, 0, true, "id")
        } returns listOf(userInClub1)

        clubViewModel.fetchUsers(true)

        assertEquals(
            listOf(userInClub1),
            clubViewModel.users.value
        )
    }

    @Test
    fun testFetchUsersLoadMore() = runBlocking {
        val logEventUseCase = mockk<ILogEventUseCase>()
        val fetchClubUseCase = mockk<IFetchClubUseCase>()
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val updateUserInClubUseCase = mockk<IUpdateUserInClubUseCase>()
        val clubViewModel =
            ClubViewModel("id", logEventUseCase, fetchClubUseCase, listUsersInClubUseCase, updateUserInClubUseCase)

        coEvery {
            listUsersInClubUseCase(25, 0, false, "id")
        } returns listOf(userInClub1)

        clubViewModel.fetchUsers()

        coEvery {
            listUsersInClubUseCase(25, 1, false, "id")
        } returns listOf(userInClub2)

        clubViewModel.fetchUsers()

        assertEquals(
            listOf(userInClub1, userInClub2),
            clubViewModel.users.value
        )
    }

    @Test
    fun testFetchUsersNotFound() = runBlocking {
        val logEventUseCase = mockk<ILogEventUseCase>()
        val fetchClubUseCase = mockk<IFetchClubUseCase>()
        val listUsersInClubUseCase = mockk<IListUsersInClubUseCase>()
        val updateUserInClubUseCase = mockk<IUpdateUserInClubUseCase>()
        val clubViewModel =
            ClubViewModel("id", logEventUseCase, fetchClubUseCase, listUsersInClubUseCase, updateUserInClubUseCase)

        coEvery {
            listUsersInClubUseCase(25, 0, true, "id")
        } throws APIException(HttpStatusCode.NotFound, "users_not_found")

        clubViewModel.fetchUsers(true)

        assertEquals(
            "users_not_found",
            clubViewModel.error.value
        )
    }
}