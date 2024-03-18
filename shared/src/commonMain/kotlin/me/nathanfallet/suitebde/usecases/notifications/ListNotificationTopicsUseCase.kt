package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class ListNotificationTopicsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IListNotificationTopicsUseCase {

    override suspend fun invoke(): NotificationTopics? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.notifications.topics(associationId)
    }

}
