package me.nathanfallet.bdeensisa.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val title: String,
    val body: String
)
