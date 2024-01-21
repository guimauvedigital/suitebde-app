package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class TicketConfiguration(
    override val id: String,
    val eventId: String,
    override val title: String?,
    override val content: String?,
    override val price: Double?,
    override val priceReduced: Double?,
    override val bail: Double?,
    val userLimit: Long?,
    val userCount: Long?,
    val event: Event? = null,
) : ShopItem {

    override val type: String = "tickets"

    val userLeft: Long?
        get() = userLimit?.let { it - (userCount ?: 0) }

}
