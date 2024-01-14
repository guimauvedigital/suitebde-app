package me.nathanfallet.suitebde.models

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val url: String?,
)
