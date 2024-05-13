package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface IUpdateUserUseCase : ITripleSuspendUseCase<String, String?, UpdateUserPayload, User?>
