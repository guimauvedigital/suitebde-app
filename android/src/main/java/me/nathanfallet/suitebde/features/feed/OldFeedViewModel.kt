package me.nathanfallet.suitebde.features.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.database.DatabaseDriverFactory
import me.nathanfallet.suitebde.extensions.SharedCacheService
import me.nathanfallet.suitebde.models.ensisa.TicketConfiguration

class OldFeedViewModel(application: Application) : AndroidViewModel(application) {

    // Properties

    private val ticketConfigurations = MutableLiveData<List<TicketConfiguration>>()

    // Getters

    fun getTicketConfigurations(): LiveData<List<TicketConfiguration>> {
        return ticketConfigurations
    }

    // Methods

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getTicketConfigurations().let {
                        ticketConfigurations.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
