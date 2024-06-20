package com.suitebde.usecases.auth

import com.suitebde.usecases.notifications.IClearFcmTokenUseCase
import dev.kaccelero.commons.auth.ILogoutUseCase

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
