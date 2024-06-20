package com.suitebde.usecases.events

import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import dev.kaccelero.usecases.ISuspendUseCase

interface ICreateEventUseCase : ISuspendUseCase<CreateEventPayload, Event?>
