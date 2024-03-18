package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ISendNotificationTokenUseCase : ISuspendUseCase<String, NotificationToken?>
