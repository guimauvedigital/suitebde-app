package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPayload(
    val token: String? = null,
    val topic: String? = null,
    val notification: Notification,
)
