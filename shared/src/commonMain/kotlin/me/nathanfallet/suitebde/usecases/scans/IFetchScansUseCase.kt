package me.nathanfallet.suitebde.usecases.scans

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IFetchScansUseCase : IPairSuspendUseCase<LocalDate, LocalDate, List<ScansForDay>>
