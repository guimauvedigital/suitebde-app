package me.nathanfallet.suitebde.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: String,
    val userId: String,
    val eventId: String,
    val configurationId: String,
    val createdAt: Instant?,
    val paid: String?,
    val user: User? = null,
    val event: Event? = null,
    val configuration: TicketConfiguration? = null,
)
