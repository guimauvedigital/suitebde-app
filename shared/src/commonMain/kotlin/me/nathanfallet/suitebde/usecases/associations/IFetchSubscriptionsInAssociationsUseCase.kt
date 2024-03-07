package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.usecases.base.ISuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination

interface IFetchSubscriptionsInAssociationsUseCase : ISuspendUseCase<Pagination, List<SubscriptionInAssociation>>
