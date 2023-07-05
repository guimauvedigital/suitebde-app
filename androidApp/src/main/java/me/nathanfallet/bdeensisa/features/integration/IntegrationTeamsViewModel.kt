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
import me.nathanfallet.bdeensisa.models.IntegrationChallenge
import me.nathanfallet.bdeensisa.models.IntegrationTeam

class IntegrationTeamsViewModel(
    application: Application,
    token: String?
) : AndroidViewModel(application) {

    // Properties

    private val teams = MutableLiveData<List<IntegrationTeam>>()
    private val challenges = MutableLiveData<List<IntegrationChallenge>>()

    // Getters

    fun getTeams(): LiveData<List<IntegrationTeam>> {
        return teams
    }

    fun getChallenges(): LiveData<List<IntegrationChallenge>> {
        return challenges
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "integration_teams")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "IntegrationTeamsView")
        }

        fetchTeams(token)
        fetchChallenges(token)
    }

    fun fetchTeams(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getIntegrationTeams(token).let {
                        teams.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchChallenges(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getIntegrationChallenges(token).let {
                        challenges.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}