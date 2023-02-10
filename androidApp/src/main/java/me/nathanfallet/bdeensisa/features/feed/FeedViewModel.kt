package me.nathanfallet.bdeensisa.features.feed

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
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.models.Topic
import me.nathanfallet.bdeensisa.services.APIService

class FeedViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private var events = MutableLiveData<List<Event>>()
    private var topics = MutableLiveData<List<Topic>>()

    // Getters

    fun getEvents(): LiveData<List<Event>> {
        return events
    }

    fun getTopics(): LiveData<List<Topic>> {
        return topics
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "feed")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "FeedView")
        }

        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                APIService().getEvents().let {
                    events.postValue(it)
                }
                APIService().getTopics().let {
                    topics.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}