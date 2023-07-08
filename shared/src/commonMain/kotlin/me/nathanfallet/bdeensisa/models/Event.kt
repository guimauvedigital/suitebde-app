package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.extensions.renderedDateTime
import me.nathanfallet.bdeensisa.extensions.renderedTime

@Serializable
data class Event(
    override val id: String,
    override val title: String?,
    override val content: String?,
    override val start: Instant?,
    override val end: Instant?,
    val topicId: String?,
    val validated: Boolean?,
    val topic: Topic? = null
) : CalendarEvent {

    val renderedDate: String
        get() {
            return if (start != null && end != null) {
                // Format start and end
                val start = start.toLocalDateTime(TimeZone.currentSystemDefault())
                val end = end.toLocalDateTime(TimeZone.currentSystemDefault())

                // Check if same day
                if (start.dayOfYear == end.dayOfYear && start.year == end.year) {
                    // Same day, format date
                    "${start.renderedDate} de ${start.renderedTime} à ${end.renderedTime}"
                } else {
                    // Different days, format date
                    "Du ${start.renderedDateTime} au ${end.renderedDateTime}"
                }
            } else {
                // No date
                "Date inconnue"
            }
        }

    override val type = CalendarEvent.CalendarEventType.EVENT

}

@Serializable
data class EventUpload(
    val title: String? = null,
    val content: String? = null,
    val start: String? = null,
    val end: String? = null,
    val topicId: String? = null,
    val validated: Boolean? = null
)
