package com.suitebde.usecases.auth

import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IUseCase

interface ISetUserIdUseCase : IUseCase<UUID?, Unit>
