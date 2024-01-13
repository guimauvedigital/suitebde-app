package me.nathanfallet.bdeensisa.features.integration

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

class IntegrationCreateViewModel(
    application: Application
) : AndroidViewModel(application) {

    // Properties

    private val name = MutableLiveData<String>()
    private val description = MutableLiveData<String>()

    // Getters

    fun getName(): LiveData<String> {
        return name
    }

    fun getDescription(): LiveData<String> {
        return description
    }

    // Setters

    fun setName(value: String) {
        name.value = value
    }

    fun setDescription(value: String) {
        description.value = value
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "integration_create")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "IntegrationCreateView")
        }
    }

    fun createTeam(token: String?, completionHandler: () -> Unit) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().postIntegrationTeams(
                        token,
                        name.value ?: "",
                        description.value ?: ""
                    )
                completionHandler()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}