package me.nathanfallet.bdeensisa.features.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.models.Event

class FeedViewModel(application: Application): AndroidViewModel(application) {

    // Properties

    private var events = MutableLiveData<List<Event>>()

    // Getters

    fun getEvents(): LiveData<List<Event>> {
        return events
    }

    // Methods

    fun load(): FeedViewModel {
        viewModelScope.launch {

        }

        return this
    }

}