package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class ClubMembership(
    val userId: String,
    val clubId: String,
    val role: String,
    val user: User? = null,
    val club: Club? = null,
)
