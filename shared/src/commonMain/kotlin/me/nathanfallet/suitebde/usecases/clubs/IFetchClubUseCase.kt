package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IFetchClubUseCase : ISuspendUseCase<String, Club?>