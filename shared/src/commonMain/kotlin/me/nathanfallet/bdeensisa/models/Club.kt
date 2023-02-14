package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Club(
    val id: String,
    val name: String,
    val description: String?,
    val information: String?,
    val createdAt: Instant?,
    val validated: Boolean?,
    val membersCount: Long?
)
