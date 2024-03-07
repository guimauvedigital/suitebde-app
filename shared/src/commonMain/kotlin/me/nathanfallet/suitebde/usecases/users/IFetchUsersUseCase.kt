package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.ISuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination

interface IFetchUsersUseCase : ISuspendUseCase<Pagination, List<User>>
