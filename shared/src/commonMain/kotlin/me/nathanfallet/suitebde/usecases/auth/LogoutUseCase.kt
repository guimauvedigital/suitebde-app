package me.nathanfallet.suitebde.usecases.auth

class LogoutUseCase(
    private val setTokenUseCase: ISetTokenUseCase,
    private val setUserIdUseCase: ISetUserIdUseCase,
    private val setAssociationIdUseCase: ISetAssociationIdUseCase,
) : ILogoutUseCase {

    override fun invoke() {
        setTokenUseCase(null)
        setUserIdUseCase(null)
        setAssociationIdUseCase(null)
        // TODO: Clear cache
    }

}
