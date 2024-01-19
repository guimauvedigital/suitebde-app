package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.extensions.renderedTime

class RenderDateRangeUseCase : IRenderDateRangeUseCase {

    override fun invoke(input1: Instant, input2: Instant): String {
        val start = input1.toLocalDateTime(TimeZone.currentSystemDefault())
        val end = input2.toLocalDateTime(TimeZone.currentSystemDefault())

        // TODO: i18n
        // Check if same day
        return if (start.dayOfYear == end.dayOfYear && start.year == end.year) {
            "${start.renderedDate} de ${start.renderedTime} à ${end.renderedTime}"
        } else {
            "Du ${start.renderedDateTime} au ${end.renderedDateTime}"
        }
    }

}
