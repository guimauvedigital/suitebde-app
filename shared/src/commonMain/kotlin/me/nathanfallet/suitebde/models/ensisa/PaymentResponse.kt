package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val url: String?,
)
