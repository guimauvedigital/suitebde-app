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
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.models.ChatConversation

class ChatViewModel(
    application: Application,
    token: String?
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

}