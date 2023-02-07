package me.nathanfallet.bdeensisa.features.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    fun load(): FeedViewModel {
        viewModelScope.launch {
            APIService().getEvents().let {
                events.postValue(it)
            }
            APIService().getTopics().let {
                topics.postValue(it)
            }
        }

        return this
    }

}