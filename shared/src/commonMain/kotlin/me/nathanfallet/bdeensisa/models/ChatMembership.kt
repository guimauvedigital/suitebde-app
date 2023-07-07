package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatMembership(
    val userId: String,
    val groupType: String,
    val groupId: String,
    val lastRead: Instant,
    val notifications: Boolean
)

@Serializable
data class ChatMembershipUpload(
    val notifications: Boolean?
)
