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
import me.nathanfallet.bdeensisa.models.ShopItem
import me.nathanfallet.bdeensisa.services.APIService

class ShopItemViewModel(
    application: Application,
    val item: ShopItem
) : AndroidViewModel(application) {

    // Properties

    private val payNow = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Boolean>()
    private val success = MutableLiveData<String>()

    // Getters

    fun getPayNow(): LiveData<Boolean> {
        return payNow
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<Boolean> {
        return error
    }

    fun getSuccess(): LiveData<String> {
        return success
    }

    // Setters

    fun setPayNow(value: Boolean) {
        payNow.postValue(value)
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "shop_item")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ShopItemView")
        }
    }

    fun launchBuy(token: String?) {
        if (token == null) {
            return
        }
        loading.postValue(true)
        viewModelScope.launch {
            try {
                APIService().createShopItem(token, item.type, item.id)
                loading.postValue(false)
                success.postValue("Votre ticket a bien été réservé. Merci de bien vouloir vous présenter à un membre du BDE pour le régler.")
                // TODO: If `payNow`, redirect to payement
            } catch (e: Exception) {
                e.printStackTrace()
                error.postValue(true)
                loading.postValue(false)
            }
        }
    }

    fun dismissError() {
        error.postValue(false)
    }

}