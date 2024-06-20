package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.NotificationToken
import dev.kaccelero.usecases.ISuspendUseCase

interface ISendNotificationTokenUseCase : ISuspendUseCase<String, NotificationToken?>
