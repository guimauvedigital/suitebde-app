package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase

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
