package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.base.ITripleSuspendUseCase

interface IFetchEventsUseCase : ITripleSuspendUseCase<Long, Long, Boolean, List<Event>>
