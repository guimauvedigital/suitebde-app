package com.suitebde.models.ensisa

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val userId: String,
    val groupType: String,
    val groupId: String,
    val type: String,
    val content: String?,
    val createdAt: Instant?,
    val user: User? = null,
)

@Serializable
data class ChatMessageUpload(
    val type: String,
    val content: String,
)
