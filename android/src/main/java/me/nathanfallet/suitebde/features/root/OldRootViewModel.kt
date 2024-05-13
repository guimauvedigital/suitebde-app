package me.nathanfallet.suitebde.features.root

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.extensions.SharedCacheService
import me.nathanfallet.suitebde.models.ensisa.ChatConversation
import me.nathanfallet.suitebde.models.ensisa.User
import me.nathanfallet.suitebde.models.ensisa.UserToken
import me.nathanfallet.suitebde.services.StorageService
import me.nathanfallet.suitebde.services.WebSocketService

class OldRootViewModel(application: Application) : AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData<User>()
    private val token = MutableLiveData<String>()

    private val selectedConversation = MutableLiveData<ChatConversation>()

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun getToken(): LiveData<String> {
        return token
    }

    fun getSelectedConversation(): LiveData<ChatConversation> {
        return selectedConversation
    }

    // Setters

    fun setUser(user: User) {
        this.user.value = user
        StorageService.getInstance(getApplication()).sharedPreferences
            .edit()
            .putString("user", User.toJson(user))
            .apply()
    }

    fun setSelectedConversation(conversation: ChatConversation) {
        selectedConversation.value = conversation
    }

    // Methods

    init {
        // Load user and token, if connected
        val prefs = StorageService.getInstance(getApplication()).sharedPreferences
        prefs.getString("user", null)?.let {
            user.value = User.fromJson(it)
            Firebase.analytics.setUserProperty(
                "custom_cotisant",
                (user.value?.cotisant != null).toString()
            )
        }
        prefs.getString("token", null)?.let {
            token.value = it // TODO: Load token in a secure way
        }

        // Check token
        checkToken()
    }

    fun checkToken() {
        token.value?.let {
            viewModelScope.launch {
                try {
                    val userToken =
                        SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                            .apiService().checkToken(it)
                    saveToken(userToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteAccount() {
        token.value?.let {
            viewModelScope.launch {
                try {
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService().deleteMe(it)
                    saveToken(null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveToken(userToken: UserToken?) {
        // Update values
        user.value = userToken?.user
        token.value = userToken?.token

        // Token is invalid, remove it
        if (userToken == null) {
            StorageService.getInstance(getApplication()).sharedPreferences
                .edit()
                .remove("user")
                .remove("token")
                .apply()
            FirebaseMessaging.getInstance().unsubscribeFromTopic("cotisants")
            return
        }

        // Else, save new values
        StorageService.getInstance(getApplication()).sharedPreferences
            .edit()
            .putString("user", User.toJson(userToken.user))
            .putString("token", userToken.token) // TODO: Save token in a secure way
            .apply()
        Firebase.analytics.setUserProperty(
            "custom_cotisant",
            (user.value?.cotisant != null).toString()
        )
        if (userToken.user.cotisant != null) {
            FirebaseMessaging.getInstance().subscribeToTopic("cotisants")
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("cotisants")
        }

        // Create (or reconnect) websocket
        WebSocketService.getInstance(getApplication()).createWebSocket()
    }

    fun onOpenURL(url: Uri) {
        // Check url for sharable data
        if (url.scheme == "bdeensisa" || url.scheme == "suitebde") {
            // Users
            if (url.host == "users") {
                //downloadUser(url.path!!.trim('/')) // TODO: Navigate to user
            }
        }
    }

}
