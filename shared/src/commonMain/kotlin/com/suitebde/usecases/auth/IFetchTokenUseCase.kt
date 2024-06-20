package com.suitebde.usecases.auth

import com.suitebde.models.auth.AuthToken
import dev.kaccelero.usecases.ISuspendUseCase

interface IFetchTokenUseCase : ISuspendUseCase<String, AuthToken?>
