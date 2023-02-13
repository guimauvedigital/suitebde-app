package me.nathanfallet.bdeensisa.models

import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    val token: String,
    val user: User
)
