package me.nathanfallet.suitebde.usecases.subscriptions

import me.nathanfallet.suitebde.models.stripe.CheckoutSession
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ICheckoutSubscriptionUseCase : ISuspendUseCase<String, CheckoutSession?>
