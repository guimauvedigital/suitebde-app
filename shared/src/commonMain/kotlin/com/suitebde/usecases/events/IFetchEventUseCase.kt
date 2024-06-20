package com.suitebde.usecases.events

import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IFetchEventUseCase : IPairSuspendUseCase<UUID, Boolean, Event?>
