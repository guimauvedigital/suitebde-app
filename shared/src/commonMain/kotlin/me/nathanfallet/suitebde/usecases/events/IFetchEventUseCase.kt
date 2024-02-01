package me.nathanfallet.suitebde.usecases.events

import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IFetchEventUseCase : IPairSuspendUseCase<String, Boolean, Event?>
