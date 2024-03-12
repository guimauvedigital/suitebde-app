package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IUpdateUserInClubUseCase : ISuspendUseCase<Club, Pair<Club, UserInClub?>?>
