package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.base.ITripleSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination

interface IListUsersInClubUseCase : ITripleSuspendUseCase<Pagination, Boolean, String, List<UserInClub>>
