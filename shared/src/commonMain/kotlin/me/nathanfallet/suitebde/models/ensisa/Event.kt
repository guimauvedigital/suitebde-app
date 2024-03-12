package me.nathanfallet.suitebde.models.ensisa

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String,
    val title: String?,
    val content: String?,
    val start: Instant?,
    val end: Instant?,
    val topicId: String?,
    val validated: Boolean?,
)
