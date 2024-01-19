package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.usecases.auth.AuthToken
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IFetchTokenUseCase : ISuspendUseCase<String, AuthToken?>
