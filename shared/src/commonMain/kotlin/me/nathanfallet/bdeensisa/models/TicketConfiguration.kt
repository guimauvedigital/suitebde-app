package me.nathanfallet.bdeensisa.models

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
    val event: Event? = null
): ShopItem
