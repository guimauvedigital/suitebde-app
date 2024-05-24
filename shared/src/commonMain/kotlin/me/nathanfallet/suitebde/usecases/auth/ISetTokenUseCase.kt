package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.usecases.auth.AuthToken
import me.nathanfallet.usecases.base.IUseCase

interface ISetTokenUseCase : IUseCase<AuthToken?, Unit>
