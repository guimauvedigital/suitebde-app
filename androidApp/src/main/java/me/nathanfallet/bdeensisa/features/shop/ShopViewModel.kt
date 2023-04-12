package me.nathanfallet.bdeensisa.features.shop

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
import me.nathanfallet.bdeensisa.models.TicketConfiguration
import me.nathanfallet.bdeensisa.services.APIService

class ShopViewModel(application: Application) : AndroidViewModel(application) {

    // Properties

    private val ticketConfigurations = MutableLiveData<List<TicketConfiguration>>()

    // Getters

    fun getTicketConfigurations(): LiveData<List<TicketConfiguration>> {
        return ticketConfigurations
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "shop")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ShopView")
        }

        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                APIService().getTicketConfigurations().let {
                    ticketConfigurations.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}