package com.suitebde.usecases.users

import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ITripleSuspendUseCase

interface IUpdateUserUseCase : ITripleSuspendUseCase<UUID, UUID?, UpdateUserPayload, User?>
