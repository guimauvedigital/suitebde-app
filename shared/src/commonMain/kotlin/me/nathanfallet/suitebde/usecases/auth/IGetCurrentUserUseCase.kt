package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IUnitSuspendUseCase

interface IGetCurrentUserUseCase : IUnitSuspendUseCase<User?>
