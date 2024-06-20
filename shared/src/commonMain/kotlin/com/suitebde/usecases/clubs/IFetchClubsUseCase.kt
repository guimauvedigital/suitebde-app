package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.Club
import dev.kaccelero.repositories.Pagination
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IFetchClubsUseCase : IPairSuspendUseCase<Pagination, Boolean, List<Club>>
