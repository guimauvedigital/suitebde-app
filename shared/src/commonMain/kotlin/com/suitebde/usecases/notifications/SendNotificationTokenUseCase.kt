package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import com.suitebde.usecases.auth.IGetUserIdUseCase

class SendNotificationTokenUseCase(
    private val client: ISuiteBDEClient,
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ISendNotificationTokenUseCase {

    override suspend fun invoke(input: String): NotificationToken? {
        val userId = getUserIdUseCase() ?: return null
        val associationId = getAssociationIdUseCase() ?: return null
        return client.notificationTokens.create(
            CreateNotificationTokenPayload(input),
            userId,
            associationId
        )
    }

}
