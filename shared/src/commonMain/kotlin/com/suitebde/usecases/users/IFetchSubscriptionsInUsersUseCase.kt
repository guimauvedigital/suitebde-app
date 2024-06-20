package com.suitebde.usecases.users

import com.suitebde.models.users.SubscriptionInUser
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IFetchSubscriptionsInUsersUseCase : IPairSuspendUseCase<UUID, UUID?, List<SubscriptionInUser>>
