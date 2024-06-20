package com.suitebde.features.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.suitebde.database.DatabaseDriverFactory
import com.suitebde.extensions.SharedCacheService
import com.suitebde.models.ensisa.ChatConversation
import com.suitebde.models.ensisa.ChatMembership
import com.suitebde.models.ensisa.ChatMessage
import com.suitebde.services.WebSocketService
import kotlinx.coroutines.launch

class ChatViewModel(
    application: Application,
    token: String?,
) : AndroidViewModel(application) {

    // Properties

    private val conversations = MutableLiveData<List<ChatConversation>>()

    // Getters

    fun getConversations(): LiveData<List<ChatConversation>> {
        return conversations
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "chat")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ChatView")
        }

        WebSocketService.getInstance(getApplication()).onWebSocketMessage = this::onWebSocketMessage
        WebSocketService.getInstance(getApplication()).currentConversationId = null
        fetchConversations(token)
    }

    fun fetchConversations(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getChat(token).let {
                        conversations.value = it.sortedByDescending { conversation ->
                            conversation.lastMessage?.createdAt?.epochSeconds ?: 0
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onWebSocketMessage(message: Any) {
        viewModelScope.launch {
            when (message) {
                is ChatMessage -> onChatMessage(message)
                is ChatMembership -> onChatMembership(message)
            }
        }
    }

    fun onChatMessage(chatMessage: ChatMessage) {
        conversations.value
            ?.filter { chatMessage.groupType == it.groupType && chatMessage.groupId == it.groupId }
            ?.forEach { it.lastMessage = chatMessage }
        conversations.value = conversations.value?.sortedByDescending { conversation ->
            conversation.lastMessage?.createdAt?.epochSeconds ?: 0
        }
    }

    fun onChatMembership(chatMembership: ChatMembership) {
        conversations.value
            ?.filter { chatMembership.groupType == it.groupType && chatMembership.groupId == it.groupId }
            ?.forEach { it.membership = chatMembership }
        conversations.value = conversations.value?.sortedByDescending { conversation ->
            conversation.lastMessage?.createdAt?.epochSeconds ?: 0
        }
    }

}
