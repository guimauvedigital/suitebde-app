package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ISendNotificationUseCase : ISuspendUseCase<CreateNotificationPayload, Unit>
