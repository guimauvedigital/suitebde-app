package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IFetchSubscriptionInAssociationUseCase : ISuspendUseCase<String, SubscriptionInAssociation?>
