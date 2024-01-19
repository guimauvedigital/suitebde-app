package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationConfiguration(
    val enabled: Boolean,
    val showRank: Boolean,
    val showScore: Boolean,
    val canCreateTeams: Boolean,
    val canCreateExecutions: Boolean,
)
