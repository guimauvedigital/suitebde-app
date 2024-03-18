package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.SubscribeToNotificationTopicType
import me.nathanfallet.usecases.base.IPairUseCase

interface ISubscribeToNotificationTopicUseCase : IPairUseCase<String, SubscribeToNotificationTopicType, Unit>
