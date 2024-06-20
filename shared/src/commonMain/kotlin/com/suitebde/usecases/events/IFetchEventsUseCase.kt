package com.suitebde.usecases.events

import com.suitebde.models.events.Event
import dev.kaccelero.repositories.Pagination
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IFetchEventsUseCase : IPairSuspendUseCase<Pagination, Boolean, List<Event>>
