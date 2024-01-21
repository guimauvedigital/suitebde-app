package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ICreateEventUseCase : ISuspendUseCase<CreateEventPayload, Event?>
