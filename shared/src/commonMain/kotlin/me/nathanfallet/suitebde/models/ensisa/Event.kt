package me.nathanfallet.suitebde.models.ensisa

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.extensions.renderedTime
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.UpdateEventPayload

@Serializable
data class Event(
    override val id: String,
    override val title: String?,
    override val content: String?,
    override val start: Instant?,
    override val end: Instant?,
    val topicId: String?,
    val validated: Boolean?,
    val topic: Topic? = null,
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

    val suiteBde = me.nathanfallet.suitebde.models.events.Event(
        id,
        "",
        title ?: "",
        content ?: "",
        null,
        start ?: Instant.DISTANT_PAST,
        end ?: Instant.DISTANT_PAST,
        validated ?: false
    )

}

@Serializable
data class EventUpload(
    val title: String? = null,
    val content: String? = null,
    val start: String? = null,
    val end: String? = null,
    val topicId: String? = null,
    val validated: Boolean? = null,
) {

    constructor(payload: CreateEventPayload) : this(
        payload.name,
        payload.description,
        payload.startsAt.toString(),
        payload.endsAt.toString(),
        null,
        payload.validated
    )

    constructor(payload: UpdateEventPayload) : this(
        payload.name,
        payload.description,
        payload.startsAt?.toString(),
        payload.endsAt?.toString(),
        null,
        payload.validated
    )

}
