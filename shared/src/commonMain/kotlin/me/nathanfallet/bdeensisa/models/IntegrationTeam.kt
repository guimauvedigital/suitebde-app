package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class IntegrationTeam(
    val id: String,
    val name: String,
    val description: String?,
    val expiration: LocalDate?,
    val membersCount: Long?,
    val score: Int?
)

@Serializable
data class IntegrationTeamUpload(
    val name: String,
    val description: String
)