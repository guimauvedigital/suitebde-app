package me.nathanfallet.suitebde.usecases.notifications

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.repositories.notifications.INotificationsRepository
import kotlin.test.Test

class ClearFcmTokenUseCaseTest {

    @Test
    fun testInvoke() {
        val tokenRepository = mockk<ITokenRepository>()
        val notificationsRepository = mockk<INotificationsRepository>()
        val useCase = ClearFcmTokenUseCase(tokenRepository, notificationsRepository)
        every { tokenRepository.setFcmToken(null) } returns Unit
        every { notificationsRepository.clearSubscriptions() } returns Unit
        useCase()
        verify { tokenRepository.setFcmToken(null) }
        verify { notificationsRepository.clearSubscriptions() }
    }

}
