package com.suitebde.usecases.scans

import com.suitebde.models.scans.ScansForDay
import dev.kaccelero.usecases.IPairSuspendUseCase
import kotlinx.datetime.LocalDate

interface IFetchScansUseCase : IPairSuspendUseCase<LocalDate, LocalDate, List<ScansForDay>>
