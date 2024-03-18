package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.SubscribeToNotificationTopicType
import me.nathanfallet.usecases.base.IPairUseCase

interface ISubscribeToNotificationTopicUseCase : IPairUseCase<String, SubscribeToNotificationTopicType, Unit>
