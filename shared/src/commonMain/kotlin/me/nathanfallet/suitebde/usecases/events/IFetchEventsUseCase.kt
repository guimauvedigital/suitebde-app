package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.base.IPairSuspendUseCase
import me.nathanfallet.usecases.pagination.Pagination

interface IFetchEventsUseCase : IPairSuspendUseCase<Pagination, Boolean, List<Event>>
