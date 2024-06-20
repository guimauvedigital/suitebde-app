package com.suitebde.usecases.auth

import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IUseCase

interface ISetAssociationIdUseCase : IUseCase<UUID?, Unit>
