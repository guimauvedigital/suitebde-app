package me.nathanfallet.suitebde.models

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val title: String,
    val body: String,
)
