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

class ClubsViewModel(
    application: Application,
    token: String?
) : AndroidViewModel(application) {

    // Properties

    private val mine = MutableLiveData<List<ClubMembership>>()
    private val clubs = MutableLiveData<List<Club>>()

    // Getters

    fun getMine(): LiveData<List<ClubMembership>> {
        return mine
    }

    fun getClubs(): LiveData<List<Club>> {
        return clubs
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "clubs")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ClubsView")
        }

        fetchData(token)
    }

    fun fetchData(token: String?) {
        viewModelScope.launch {
            try {
                APIService().getClubs().let {
                    clubs.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                APIService().getClubsMe(token).let {
                    mine.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}