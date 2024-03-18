package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.usecases.notifications.IClearFcmTokenUseCase

class LogoutUseCase(
    private val setTokenUseCase: ISetTokenUseCase,
    private val setUserIdUseCase: ISetUserIdUseCase,
    private val setAssociationIdUseCase: ISetAssociationIdUseCase,
    private val clearFcmTokenUseCase: IClearFcmTokenUseCase,
) : ILogoutUseCase {

    override fun invoke() {
        setTokenUseCase(null)
        setUserIdUseCase(null)
        setAssociationIdUseCase(null)
        clearFcmTokenUseCase()
        // TODO: Clear cache
    }

}
