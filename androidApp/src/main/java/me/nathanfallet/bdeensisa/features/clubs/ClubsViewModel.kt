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
    private val hasMore = MutableLiveData(true)

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

        fetchClubs(true)
        fetchMine(token)
    }

    fun fetchClubs(reset: Boolean) {
        viewModelScope.launch {
            try {
                APIService().getClubs(
                    (if (reset) 0 else clubs.value?.size ?: 0).toLong()
                ).let {
                    if (reset) {
                        clubs.postValue(it)
                    } else {
                        clubs.postValue((clubs.value ?: listOf()) + it)
                    }
                    hasMore.postValue(it.isNotEmpty())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchMine(token: String?) {
        token?.let {
            viewModelScope.launch {
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

    fun loadMore(token: String?, id: String?) {
        if (hasMore.value != true) {
            return
        }
        token?.let {
            if (clubs.value?.lastOrNull()?.id == id) {
                fetchClubs(false)
            }
        }
    }

    fun joinClub(id: String, token: String?) {
        token?.let {
            viewModelScope.launch {
                try {
                    APIService().joinClub(token, id)
                    fetchMine(token)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}