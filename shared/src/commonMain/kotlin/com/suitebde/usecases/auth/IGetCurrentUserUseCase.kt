package com.suitebde.usecases.auth

import com.suitebde.models.users.User
import dev.kaccelero.usecases.IUnitSuspendUseCase

interface IGetCurrentUserUseCase : IUnitSuspendUseCase<User?>
