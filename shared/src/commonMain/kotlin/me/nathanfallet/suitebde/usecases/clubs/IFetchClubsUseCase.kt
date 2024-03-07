package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface IFetchClubsUseCase : ITripleSuspendUseCase<Long, Long, Boolean, List<Club>>