package me.nathanfallet.suitebde.features.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.extensions.SharedCacheService
import me.nathanfallet.suitebde.models.ensisa.ChatConversation
import me.nathanfallet.suitebde.models.ensisa.ChatMessage
import me.nathanfallet.suitebde.models.ensisa.ChatMessageUpload
import me.nathanfallet.suitebde.models.ensisa.User
import me.nathanfallet.suitebde.services.WebSocketService
import java.util.*

class ConversationViewModel(
    application: Application,
    token: String?,
    val conversation: ChatConversation,
) : AndroidViewModel(application) {

    // Properties

    private val messages = MutableLiveData<List<ChatMessage>>()
    private val hasMore = MutableLiveData(true)
    private val sendingMessages = MutableLiveData<List<ChatMessage>>()
    private val typingMessage = MutableLiveData("")

    // Getters

    fun getMessages(): LiveData<List<ChatMessage>> {
        return messages
    }

    fun getSendingMessages(): LiveData<List<ChatMessage>> {
        return sendingMessages
    }

    fun getTypingMessage(): LiveData<String> {
        return typingMessage
    }

    // Setters

    fun setTypingMessage(message: String) {
        typingMessage.value = message
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "conversation")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ConversationView")
        }

        WebSocketService.getInstance(getApplication()).onWebSocketMessageConversation =
            this::onWebSocketMessage
        WebSocketService.getInstance(getApplication()).currentConversationId = conversation.id
        fetchMessages(token, true)
    }

    fun fetchMessages(token: String?, reset: Boolean) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getChatMessages(
                        token,
                        conversation.groupType,
                        conversation.groupId,
                        (if (reset) 0 else messages.value?.size ?: 0).toLong()
                    ).let {
                        if (reset) {
                            messages.value = it
                        } else {
                            messages.value = (messages.value ?: listOf()) + it
                        }
                        hasMore.value = it.isNotEmpty()
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMore(token: String?, id: String?) {
        if (hasMore.value != true) {
            return
        }
        if (messages.value?.lastOrNull()?.id == id) {
            fetchMessages(token, false)
        }
    }

    fun sendMessage(token: String?, sentBy: User?) {
        if (token == null || sentBy == null) {
            return
        }
        val futureMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            userId = sentBy.id,
            groupType = conversation.groupType,
            groupId = conversation.groupId,
            type = "text",
            content = typingMessage.value ?: "",
            createdAt = Clock.System.now(),
            user = sentBy
        )
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .postChatMessages(
                        token,
                        conversation.groupType,
                        conversation.groupId,
                        ChatMessageUpload(
                            type = futureMessage.type,
                            content = futureMessage.content ?: ""
                        )
                    )
                sendingMessages.value =
                    sendingMessages.value?.filter { m -> m.id != futureMessage.id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        sendingMessages.value = (sendingMessages.value ?: listOf()) + futureMessage
        typingMessage.value = ""
    }

    fun markAsRead(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .putChatMembers(
                        token,
                        conversation.groupType,
                        conversation.groupId,
                        null
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onWebSocketMessage(message: Any) {
        viewModelScope.launch {
            when (message) {
                is ChatMessage -> onChatMessage(message)
            }
        }
    }

    fun onChatMessage(chatMessage: ChatMessage) {
        if (chatMessage.groupType != conversation.groupType || chatMessage.groupId != conversation.groupId) {
            return
        }
        messages.value = listOf(chatMessage) + (messages.value ?: listOf())
    }

}
