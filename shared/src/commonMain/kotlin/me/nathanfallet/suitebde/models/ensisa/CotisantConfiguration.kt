package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class CotisantConfiguration(
    override val id: String,
    override val title: String?,
    override val content: String?,
    override val price: Double?,
    val years: Int?,
) : ShopItem {

    override val type: String = "cotisants"

    override val priceReduced: Double?
        get() = price

    override val bail: Double
        get() = 0.0

    override val canPayLater: Boolean = false

    val suiteBde = me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation(
        id,
        "",
        title ?: "",
        content ?: "",
        price ?: 0.0,
        if (years != null && years > 0) "${years}y" else "1d",
        false
    )

}
