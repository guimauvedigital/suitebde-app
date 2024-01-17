package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IFetchEventsUseCase : IPairSuspendUseCase<Long, Long, List<Event>>
