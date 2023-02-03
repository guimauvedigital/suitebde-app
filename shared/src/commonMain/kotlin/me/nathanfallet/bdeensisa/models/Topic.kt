package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: String,
    val userId: String,
    val title: String?,
    val content: String?,
    val createdAt: Instant?,
    val validated: Boolean?,
    val user: User? = null
)
