package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.CreateNotificationPayload
import dev.kaccelero.usecases.ISuspendUseCase

interface ISendNotificationUseCase : ISuspendUseCase<CreateNotificationPayload, Unit>
