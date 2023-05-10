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
    val id: String,
    val title: String?,
    val content: String?,
    val start: Instant?,
    val end: Instant?,
    val topicId: String?,
    val validated: Boolean?,
    val topic: Topic? = null
) {

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

}
