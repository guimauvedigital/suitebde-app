package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class IntegrationExecution(
    val id: String,
    val teamId: String,
    val challengeId: String,
    val userId: String,
    val validated: Boolean,
    val proof: String,
    val createdAt: Instant,
    val user: User? = null,
    val team: IntegrationTeam? = null,
    val challenge: IntegrationChallenge? = null
)