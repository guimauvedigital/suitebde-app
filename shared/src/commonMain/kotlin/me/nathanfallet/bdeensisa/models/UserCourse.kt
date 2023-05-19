package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserCourse(
    val adeUid: String,
    val userId: String,
    override val title: String?,
    override val start: Instant?,
    override val end: Instant?,
    val location: String?,
    val description: String?,
    val user: User? = null
) : CalendarEvent {

    override val id: String
        get() = adeUid

    override val content: String
        get() = "$location, $description"

    override val type = CalendarEvent.CalendarEventType.USER_COURSE

}