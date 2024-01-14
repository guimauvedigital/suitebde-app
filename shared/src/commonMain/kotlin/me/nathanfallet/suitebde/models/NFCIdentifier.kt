package me.nathanfallet.suitebde.models

import kotlinx.serialization.Serializable

@Serializable
data class NFCIdentifier(
    val id: String,
    val userId: String,
    val user: User? = null,
)
