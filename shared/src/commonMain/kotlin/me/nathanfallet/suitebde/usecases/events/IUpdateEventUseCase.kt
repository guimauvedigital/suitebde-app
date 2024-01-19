package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.UpdateEventPayload
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IUpdateEventUseCase : IPairSuspendUseCase<String, UpdateEventPayload, Event?>
