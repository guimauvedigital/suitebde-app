package me.nathanfallet.bdeensisa.features.clubs

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
import me.nathanfallet.bdeensisa.models.Club
import me.nathanfallet.bdeensisa.models.ClubMembership
import me.nathanfallet.bdeensisa.services.APIService

class ClubViewModel(
    application: Application,
    val club: Club
) : AndroidViewModel(application) {

    // Properties

    private val members = MutableLiveData<List<ClubMembership>>()

    // Getters

    fun getMembers(): LiveData<List<ClubMembership>> {
        return members
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "club")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ClubView")
        }

        fetchMembers()
    }

    fun fetchMembers() {
        viewModelScope.launch {
            try {
                APIService().getClubMembers(club.id).let {
                    members.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun join(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                APIService().joinClub(token, club.id)
                fetchMembers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun leave(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                APIService().leaveClub(token, club.id)
                fetchMembers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
