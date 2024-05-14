package me.nathanfallet.suitebde.models.application

import kotlinx.serialization.Serializable

@Serializable
data class ScannedUser(
    val associationId: String,
    val userId: String,
)
