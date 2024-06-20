package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import dev.kaccelero.usecases.ITripleSuspendUseCase

interface IListUsersInClubUseCase : ITripleSuspendUseCase<Pagination, Boolean, UUID, List<UserInClub>>
