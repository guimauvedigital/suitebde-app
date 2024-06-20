package com.suitebde.extensions

import com.suitebde.models.events.Event
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val Event.renderedDate: String
    get() {
        val start = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        val end = endsAt.toLocalDateTime(TimeZone.currentSystemDefault())

        // TODO: i18n
        // Check if same day
        return if (start.dayOfYear == end.dayOfYear && start.year == end.year) {
            "${start.renderedDate} de ${start.renderedTime} à ${end.renderedTime}"
        } else {
            "Du ${start.renderedDateTime} au ${end.renderedDateTime}"
        }
    }
