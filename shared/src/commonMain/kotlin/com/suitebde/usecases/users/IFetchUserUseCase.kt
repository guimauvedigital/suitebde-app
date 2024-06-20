package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IFetchUserUseCase : IPairSuspendUseCase<UUID, UUID?, User?>
