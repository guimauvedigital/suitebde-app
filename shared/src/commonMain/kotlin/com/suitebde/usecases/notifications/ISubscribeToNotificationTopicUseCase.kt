package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.SubscribeToNotificationTopicType
import dev.kaccelero.usecases.IPairUseCase

interface ISubscribeToNotificationTopicUseCase : IPairUseCase<String, SubscribeToNotificationTopicType, Unit>
