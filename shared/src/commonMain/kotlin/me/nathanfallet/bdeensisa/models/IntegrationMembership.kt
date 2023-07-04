package me.nathanfallet.bdeensisa.models

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationMembership(
    val userId: String,
    val teamId: String,
    val role: String,
    val user: User? = null,
    val team: IntegrationTeam? = null
)