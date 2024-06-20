package com.suitebde.models.application

import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class ScannedUser(
    val associationId: UUID,
    val userId: UUID,
)
