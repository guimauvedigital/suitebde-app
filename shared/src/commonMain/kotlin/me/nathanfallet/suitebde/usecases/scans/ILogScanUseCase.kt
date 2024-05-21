package me.nathanfallet.suitebde.usecases.scans

import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ILogScanUseCase : ISuspendUseCase<CreateScanPayload, Scan?>
