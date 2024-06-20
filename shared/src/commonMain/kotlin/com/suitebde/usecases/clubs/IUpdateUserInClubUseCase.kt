package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.usecases.ISuspendUseCase

interface IUpdateUserInClubUseCase : ISuspendUseCase<Club, Pair<Club, UserInClub?>?>
