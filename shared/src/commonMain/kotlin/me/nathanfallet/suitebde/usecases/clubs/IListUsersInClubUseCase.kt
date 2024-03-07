package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.base.IQuadSuspendUseCase

interface IListUsersInClubUseCase : IQuadSuspendUseCase<Long, Long, Boolean, String, List<UserInClub>>