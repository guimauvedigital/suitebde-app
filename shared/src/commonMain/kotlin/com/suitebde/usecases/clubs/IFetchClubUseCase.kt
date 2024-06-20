package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.Club
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IFetchClubUseCase : ISuspendUseCase<UUID, Club?>
