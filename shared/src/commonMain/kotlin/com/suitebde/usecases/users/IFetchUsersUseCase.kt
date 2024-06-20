package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.repositories.Pagination
import dev.kaccelero.usecases.ISuspendUseCase

interface IFetchUsersUseCase : ISuspendUseCase<Pagination, List<User>>
