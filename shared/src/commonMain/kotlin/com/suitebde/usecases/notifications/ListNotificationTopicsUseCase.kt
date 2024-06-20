package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.NotificationTopics
import com.suitebde.usecases.auth.IGetAssociationIdUseCase

class ListNotificationTopicsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IListNotificationTopicsUseCase {

    override suspend fun invoke(): NotificationTopics? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.notifications.topics(associationId)
    }

}
