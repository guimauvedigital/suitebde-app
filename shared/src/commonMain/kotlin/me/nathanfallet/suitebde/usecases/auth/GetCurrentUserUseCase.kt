package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.usecases.users.IFetchUserUseCase

class GetCurrentUserUseCase(
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val fetchUserUseCase: IFetchUserUseCase,
) : IGetCurrentUserUseCase {

    // TODO: Save to cache (but fetch if possible, to avoid cache inconsistency)
    override suspend fun invoke() = getUserIdUseCase()?.let { fetchUserUseCase(it, null) }

}
