package com.suitebde.usecases.associations

import com.suitebde.models.associations.SubscriptionInAssociation
import dev.kaccelero.repositories.Pagination
import dev.kaccelero.usecases.ISuspendUseCase

interface IFetchSubscriptionsInAssociationsUseCase : ISuspendUseCase<Pagination, List<SubscriptionInAssociation>>
