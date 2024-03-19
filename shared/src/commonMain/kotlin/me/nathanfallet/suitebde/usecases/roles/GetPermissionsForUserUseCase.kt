package me.nathanfallet.suitebde.usecases.roles

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User

class GetPermissionsForUserUseCase(
    private val client: ISuiteBDEClient,
) : IGetPermissionsForUserUseCase {

    override suspend fun invoke(input: User): Set<Permission> =
        client.users.listPermissions(input.id, input.associationId).toSet()

}
