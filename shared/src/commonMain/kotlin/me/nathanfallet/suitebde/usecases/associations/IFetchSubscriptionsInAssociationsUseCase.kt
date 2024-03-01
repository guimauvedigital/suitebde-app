package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IFetchSubscriptionsInAssociationsUseCase : IPairSuspendUseCase<Long, Long, List<SubscriptionInAssociation>>
