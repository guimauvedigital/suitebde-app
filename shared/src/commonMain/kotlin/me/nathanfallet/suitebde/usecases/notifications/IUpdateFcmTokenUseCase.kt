package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.usecases.base.ISuspendUseCase

interface IUpdateFcmTokenUseCase : ISuspendUseCase<String, Unit>
