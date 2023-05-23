package me.nathanfallet.bdeensisa.models

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    val url: String?
)
