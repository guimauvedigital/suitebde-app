package com.suitebde.viewmodels.clubs

import com.suitebde.models.clubs.Club
import kotlinx.datetime.Clock

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
