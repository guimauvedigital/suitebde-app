package me.nathanfallet.suitebde.models

import kotlinx.serialization.Serializable

@Serializable
data class IntegrationConfiguration(
    val enabled: Boolean,
    val showRank: Boolean,
    val showScore: Boolean,
    val canCreateTeams: Boolean,
    val canCreateExecutions: Boolean,
)
