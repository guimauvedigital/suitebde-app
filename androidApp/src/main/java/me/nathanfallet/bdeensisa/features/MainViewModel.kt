package me.nathanfallet.bdeensisa.features

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
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken
import me.nathanfallet.bdeensisa.services.APIService
import me.nathanfallet.bdeensisa.services.StorageService

class MainViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData<User>()
    private val token = MutableLiveData<String>()

    private val selectedUser = MutableLiveData<User>()

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun getToken(): LiveData<String> {
        return token
    }

    fun getSelectedUser(): LiveData<User> {
        return selectedUser
    }

    // Setters

    fun setSelectedUser(user: User) {
        selectedUser.value = user
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

        // Setup firebase messaging
        setupFirebaseMessaging()
    }

    fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                token.value?.let { token ->
                    it.result?.let { fcmToken ->
                        viewModelScope.launch {
                            APIService().sendNotificationToken(
                                token,
                                fcmToken
                            )
                        }
                    }
                }
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("broadcast")
    }

    fun checkToken() {
        token.value?.let {
            viewModelScope.launch {
                val userToken = APIService().checkToken(it)
                saveToken(userToken)
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
    }

    fun onOpenURL(url: Uri) {
        // Check url for sharable data
        if (url.scheme == "bdeensisa") {
            // Users
            if (url.host == "users") {
                downloadUser(url.path!!.trim('/'))
            }
        }
    }

    fun downloadUser(id: String) {
        token.value?.let { token ->
            viewModelScope.launch {
                selectedUser.value = APIService().getUser(token, id)
            }
        }
    }
}