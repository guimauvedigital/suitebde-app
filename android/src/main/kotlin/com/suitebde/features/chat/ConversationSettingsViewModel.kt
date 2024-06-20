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
import com.suitebde.models.ensisa.ChatMembershipUpload
import com.suitebde.models.ensisa.User
import kotlinx.coroutines.launch

class ConversationSettingsViewModel(
    application: Application,
    token: String?,
    val conversation: ChatConversation,
) : AndroidViewModel(application) {

    // Properties

    private val notifications = MutableLiveData<Boolean>()
    private val members = MutableLiveData<List<User>>()

    // Getters

    fun getNotifications(): LiveData<Boolean> {
        return notifications
    }

    fun getMembers(): LiveData<List<User>> {
        return members
    }

    // Setters

    fun setNotifications(value: Boolean) {
        notifications.value = value
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "conversation_settings")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ConversationSettingsView")
        }

        notifications.value = conversation.membership?.notifications ?: true
        fetchMembers(token)
    }

    fun fetchMembers(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getChatMembers(
                        token,
                        conversation.groupType,
                        conversation.groupId
                    ).let {
                        members.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMembership(token: String?) {
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
                        ChatMembershipUpload(
                            notifications.value ?: true
                        )
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
