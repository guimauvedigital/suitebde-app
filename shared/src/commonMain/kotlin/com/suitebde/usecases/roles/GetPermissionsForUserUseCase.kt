package com.suitebde.usecases.roles

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User

class GetPermissionsForUserUseCase(
    private val client: ISuiteBDEClient,
) : IGetPermissionsForUserUseCase {

    override suspend fun invoke(input: User): Set<Permission> =
        client.users.listPermissions(input.id, input.associationId).toSet()

}
