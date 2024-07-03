package com.suitebde.usecases.users

import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IDeleteUserUseCase : IPairSuspendUseCase<UUID, UUID?, Boolean>
