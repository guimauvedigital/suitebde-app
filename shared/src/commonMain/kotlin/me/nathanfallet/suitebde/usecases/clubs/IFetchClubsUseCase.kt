package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.usecases.base.IPairSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination

interface IFetchClubsUseCase : IPairSuspendUseCase<Pagination, Boolean, List<Club>>
