package com.suitebde.usecases.events

import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IUpdateEventUseCase : IPairSuspendUseCase<UUID, UpdateEventPayload, Event?>
