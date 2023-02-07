package me.nathanfallet.bdeensisa.features

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken
import me.nathanfallet.bdeensisa.services.APIService
import me.nathanfallet.bdeensisa.services.StorageService

class MainViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData<User>()
    private val token = MutableLiveData<String>()

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun getToken(): LiveData<String> {
        return token
    }

    // Methods

    fun load(): MainViewModel {
        // Load user and token, if connected
        val prefs = StorageService.getInstance(getApplication()).sharedPreferences
        prefs.getString("user", null)?.let {
            user.value = User.fromJson(it)
        }
        prefs.getString("token", null)?.let {
            token.value = it // TODO: Load token in a secure way
        }

        // Check token
        checkToken()

        return this
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
                val user = APIService().getUser(token, id)
            }
        }
    }
}