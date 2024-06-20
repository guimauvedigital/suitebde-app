package com.suitebde.usecases.associations

import com.suitebde.models.associations.SubscriptionInAssociation
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IFetchSubscriptionInAssociationUseCase : ISuspendUseCase<UUID, SubscriptionInAssociation?>
