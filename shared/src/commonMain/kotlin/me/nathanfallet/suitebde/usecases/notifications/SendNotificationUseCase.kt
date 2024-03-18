package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class SendNotificationUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ISendNotificationUseCase {

    override suspend fun invoke(input: CreateNotificationPayload) {
        val associationId = getAssociationIdUseCase() ?: return
        client.notifications.send(input, associationId)
    }

}
