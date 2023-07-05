package me.nathanfallet.bdeensisa.features.integration

import android.app.Application
import android.content.Context
import android.net.Uri
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
import java.util.UUID

class IntegrationExecutionViewModel(
    application: Application,
    token: String?,
    val team: IntegrationTeam
) : AndroidViewModel(application) {

    // Properties

    private val challenges = MutableLiveData<List<IntegrationChallenge>>()
    private val challenge = MutableLiveData<String>()
    private val filename = MutableLiveData<String>()
    private var filedata: ByteArray? = null

    // Getters

    fun getChallenges(): LiveData<List<IntegrationChallenge>> {
        return challenges
    }

    fun getChallenge(): LiveData<String> {
        return challenge
    }

    fun getFilename(): LiveData<String> {
        return filename
    }

    // Setters

    fun setChallenge(value: String) {
        challenge.value = value
    }

    fun setFilename(value: String) {
        filename.value = value
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "integration_execution")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "IntegrationExecutionView")
        }

        fetchChallenges(token)
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

    fun createExecution(token: String?, completionHandler: () -> Unit) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().postIntegrationTeamExecution(
                        token,
                        team.id,
                        challenge.value ?: "",
                        filedata ?: ByteArray(0),
                        filename.value ?: ""
                    )
                completionHandler()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun imageSelected(uri: Uri?, context: Context) {
        if (uri == null) {
            return
        }
        viewModelScope.launch {
            try {
                filedata = context.contentResolver.openInputStream(uri)?.use {
                    it.readBytes()
                }
                val extension = context.contentResolver.getType(uri)?.split("/")?.lastOrNull()
                filename.value =
                    "${uri.lastPathSegment ?: UUID.randomUUID().toString()}.${extension ?: ".jpeg"}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}