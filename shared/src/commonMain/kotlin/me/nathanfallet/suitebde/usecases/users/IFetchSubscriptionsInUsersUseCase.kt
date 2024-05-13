package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IFetchSubscriptionsInUsersUseCase : IPairSuspendUseCase<String, String?, List<SubscriptionInUser>>
