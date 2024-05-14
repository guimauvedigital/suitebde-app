package me.nathanfallet.suitebde.features.root

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.nathanfallet.suitebde.models.ensisa.ChatConversation
import me.nathanfallet.suitebde.models.ensisa.User
import me.nathanfallet.suitebde.services.StorageService

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

    fun deleteAccount() {}

}
