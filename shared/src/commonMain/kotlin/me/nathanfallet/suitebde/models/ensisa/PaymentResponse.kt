package me.nathanfallet.suitebde.models.ensisa

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.models.stripe.CheckoutSession

@Serializable
data class PaymentResponse(
    val url: String?,
) {

    val suiteBde = CheckoutSession(
        id = "",
        url = url!!,
    )

}
