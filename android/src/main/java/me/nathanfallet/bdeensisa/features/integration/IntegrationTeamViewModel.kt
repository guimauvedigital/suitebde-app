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
import me.nathanfallet.bdeensisa.models.IntegrationExecution
import me.nathanfallet.bdeensisa.models.IntegrationMembership
import me.nathanfallet.bdeensisa.models.IntegrationTeam
import me.nathanfallet.bdeensisa.models.User

class IntegrationTeamViewModel(
    application: Application,
    token: String?,
    viewedBy: User?,
    val team: IntegrationTeam
) : AndroidViewModel(application) {

    // Properties

    private val members = MutableLiveData<List<IntegrationMembership>>()
    private val executions = MutableLiveData<List<IntegrationExecution>>()
    private val member = MutableLiveData<Boolean>()

    // Getters

    fun getMembers(): LiveData<List<IntegrationMembership>> {
        return members
    }

    fun getExecutions(): LiveData<List<IntegrationExecution>> {
        return executions
    }

    fun getMember(): LiveData<Boolean> {
        return member
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "integration_team")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "IntegrationTeamView")
        }

        fetchMembers(token, viewedBy)
        fetchExecutions(token)
    }

    fun fetchMembers(token: String?, viewedBy: User?) {
        if (token == null || viewedBy == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getIntegrationTeamMembers(token, team.id).let {
                        members.value = it
                        member.value = it.any { m -> m.userId == viewedBy.id }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchExecutions(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getIntegrationTeamExecutions(token, team.id).let {
                        executions.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun join(token: String?, viewedBy: User?) {
        if (token == null || viewedBy == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .joinIntegrationTeam(token, team.id)
                fetchMembers(token, viewedBy)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun leave(token: String?, viewedBy: User?) {
        if (token == null || viewedBy == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .leaveIntegrationTeam(token, team.id)
                fetchMembers(token, viewedBy)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}