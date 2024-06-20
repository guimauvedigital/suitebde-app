package com.suitebde.usecases.scans

import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import dev.kaccelero.usecases.ISuspendUseCase

interface ILogScanUseCase : ISuspendUseCase<CreateScanPayload, Scan?>
