package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationChallenge(
    val id: String,
    val name: String,
    val description: String,
    val reward: Int,
    val executionsPerTeam: Int?,
    val executionsPerUser: Int?,
)
