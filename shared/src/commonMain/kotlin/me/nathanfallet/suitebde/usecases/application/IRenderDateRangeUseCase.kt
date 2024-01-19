package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.usecases.base.IPairUseCase

interface IRenderDateRangeUseCase : IPairUseCase<Instant, Instant, String>
