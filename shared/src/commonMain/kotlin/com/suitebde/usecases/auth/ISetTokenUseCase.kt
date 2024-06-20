package com.suitebde.usecases.auth

import com.suitebde.models.auth.AuthToken
import dev.kaccelero.usecases.IUseCase

interface ISetTokenUseCase : IUseCase<AuthToken?, Unit>
