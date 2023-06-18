package me.nathanfallet.bdeensisa.models

import kotlinx.serialization.Serializable

@Serializable
data class NFCIdentifier(
    val id: String,
    val userId: String,
    val user: User? = null
)
