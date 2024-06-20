package com.suitebde.models.ensisa

import kotlinx.serialization.Serializable

@Serializable
data class ChatConversation(
    val groupType: String,
    val groupId: String,
    val name: String,
    val logo: String? = null,
    var backupLogo: String? = null,
    var membership: ChatMembership? = null,
    var lastMessage: ChatMessage? = null,
) {

    val id: String
        get() {
            return "$groupType/$groupId"
        }

    val isUnread: Boolean
        get() {
            return lastMessage?.createdAt?.let { createdAt ->
                membership?.lastRead?.let { lastRead ->
                    createdAt > lastRead
                } ?: true
            } ?: false
        }

}
