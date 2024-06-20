package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.usecases.auth.IGetAssociationIdUseCase

class SendNotificationUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ISendNotificationUseCase {

    override suspend fun invoke(input: CreateNotificationPayload) {
        val associationId = getAssociationIdUseCase() ?: return
        client.notifications.send(input, associationId)
    }

}
