package me.nathanfallet.bdeensisa.features.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.models.ChatConversation
import me.nathanfallet.bdeensisa.models.ChatMessage
import me.nathanfallet.bdeensisa.models.ChatMessageUpload
import me.nathanfallet.bdeensisa.models.User
import java.util.UUID

class ConversationViewModel(
    application: Application,
    token: String?,
    val conversation: ChatConversation
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

}