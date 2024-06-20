package com.suitebde.usecases.associations

import com.suitebde.models.stripe.CheckoutSession
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface ICheckoutSubscriptionUseCase : ISuspendUseCase<UUID, CheckoutSession?>
