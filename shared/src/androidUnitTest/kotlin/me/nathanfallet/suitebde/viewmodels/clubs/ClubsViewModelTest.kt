package me.nathanfallet.suitebde.viewmodels.clubs

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.clubs.Club

class ClubsViewModelTest {
    private val club1 = Club(
        id = "club1",
        associationId = "associationId",
        name = "name",
        description = "description",
        logo = null,
        createdAt = Clock.System.now(),
        validated = true,
        usersCount = 0,
        isMember = true
    )

    private val club2 = Club(
        id = "club2",
        associationId = "associationId",
        name = "name",
        description = "description",
        logo = null,
        createdAt = Clock.System.now(),
        validated = true,
        usersCount = 0,
        isMember = false
    )
}