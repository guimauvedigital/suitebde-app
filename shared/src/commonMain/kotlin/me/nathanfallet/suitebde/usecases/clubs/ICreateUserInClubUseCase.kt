package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface ICreateUserInClubUseCase : IPairSuspendUseCase<CreateUserInClubPayload, String, UserInClub?>