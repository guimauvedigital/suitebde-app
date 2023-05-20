package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant

interface CalendarEvent {

    val id: String
    val title: String?
    val start: Instant?
    val end: Instant?
    val content: String?
    val type: CalendarEventType

    enum class CalendarEventType {
        EVENT, USER_COURSE
    }

}
