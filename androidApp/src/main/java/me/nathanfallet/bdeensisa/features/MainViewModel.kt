package me.nathanfallet.bdeensisa.features

import android.app.Application
import android.net.Uri
import android.nfc.Tag
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.extensions.formattedIdentifier
import me.nathanfallet.bdeensisa.models.Club
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.models.IntegrationConfiguration
import me.nathanfallet.bdeensisa.models.IntegrationTeam
import me.nathanfallet.bdeensisa.models.NFCMode
import me.nathanfallet.bdeensisa.models.ShopItem
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken
import me.nathanfallet.bdeensisa.services.StorageService

class MainViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData<User>()
    private val token = MutableLiveData<String>()

    private val integrationConfiguration = MutableLiveData<IntegrationConfiguration>()
    private val nfcMode = MutableLiveData<NFCMode>()
    private val showAccount = MutableLiveData<Unit>()
    private val selectedUser = MutableLiveData<User>()
    private val selectedEvent = MutableLiveData<Event>()
    private val selectedClub = MutableLiveData<Club>()
    private val selectedShopItem = MutableLiveData<ShopItem>()
    private val selectedIntegrationTeam = MutableLiveData<IntegrationTeam>()

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun getToken(): LiveData<String> {
        return token
    }

    fun getIntegrationConfiguration(): LiveData<IntegrationConfiguration> {
        return integrationConfiguration
    }

    fun getNFCMode(): LiveData<NFCMode> {
        return nfcMode
    }

    fun getShowAccount(): LiveData<Unit> {
        return showAccount
    }

    fun getSelectedUser(): LiveData<User> {
        return selectedUser
    }

    fun getSelectedEvent(): LiveData<Event> {
        return selectedEvent
    }

    fun getSelectedClub(): LiveData<Club> {
        return selectedClub
    }

    fun getSelectedShopItem(): LiveData<ShopItem> {
        return selectedShopItem
    }

    fun getSelectedIntegrationTeam(): LiveData<IntegrationTeam> {
        return selectedIntegrationTeam
    }

    // Setters

    fun setUser(user: User) {
        this.user.value = user
        StorageService.getInstance(getApplication()).sharedPreferences
            .edit()
            .putString("user", User.toJson(user))
            .apply()
    }

    fun setNFCMode(mode: NFCMode) {
        nfcMode.value = mode
    }

    fun showAccount() {
        showAccount.postValue(Unit)
    }

    fun setSelectedUser(user: User) {
        selectedUser.value = user
    }

    fun setSelectedEvent(event: Event) {
        selectedEvent.value = event
    }

    fun setSelectedClub(club: Club) {
        selectedClub.value = club
    }

    fun setSelectedShopItem(item: ShopItem) {
        selectedShopItem.value = item
    }

    fun setSelectedIntegrationTeam(team: IntegrationTeam) {
        selectedIntegrationTeam.value = team
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
        fetchData()

        // Setup firebase messaging
        setupFirebaseMessaging()
    }

    fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                token.value?.let { token ->
                    it.result?.let { fcmToken ->
                        viewModelScope.launch {
                            try {
                                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                                    .apiService().sendNotificationToken(
                                    token,
                                    fcmToken
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("broadcast")
        updateSubscription("events")
    }

    fun updateSubscription(topic: String) {
        if (StorageService.getInstance(getApplication()).sharedPreferences.getBoolean(
                "notifications_$topic",
                true
            )
        ) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        }
    }

    fun fetchData() {
        fetchIntegrationConfiguration()
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

    fun fetchIntegrationConfiguration() {
        token.value?.let {
            viewModelScope.launch {
                try {
                    integrationConfiguration.value =
                        SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                            .apiService().getIntegrationConfiguration(it)
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
                    showAccount()
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
    }

    fun onOpenURL(url: Uri) {
        // Check url for sharable data
        if (url.scheme == "bdeensisa") {
            // Users
            if (url.host == "users") {
                downloadUser(url.path!!.trim('/'))
            } else if (url.host == "nfc") {
                downloadUserByNFC(url.path!!.trim('/'))
            }
        }
    }

    fun nfcResult(tag: Tag) {
        val tagId = tag.formattedIdentifier
        when (nfcMode.value) {
            NFCMode.READ -> downloadUserByNFC(tagId)
            NFCMode.UPDATE -> sendNFCResult(tagId)
            else -> {}
        }
    }

    fun sendNFCResult(tagId: String) {
        token.value?.let { token ->
            viewModelScope.launch {
                try {
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService().postNFC(token, tagId)
                        .let {
                            user.value = it
                            nfcMode.value = null
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun downloadUser(id: String) {
        token.value?.let { token ->
            viewModelScope.launch {
                try {
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService().getUser(token, id)
                        .let {
                            selectedUser.value = it
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun downloadUserByNFC(id: String) {
        token.value?.let { token ->
            viewModelScope.launch {
                try {
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService().getUserByNFC(token, id)
                        .let {
                            selectedUser.value = it
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}