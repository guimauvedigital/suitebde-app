package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val title: String,
    val body: String,
)
