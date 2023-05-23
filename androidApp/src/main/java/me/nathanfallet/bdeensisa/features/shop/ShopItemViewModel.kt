package me.nathanfallet.bdeensisa.features.shop

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
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
import me.nathanfallet.bdeensisa.models.ShopItem

class ShopItemViewModel(
    application: Application,
    val item: ShopItem
) : AndroidViewModel(application) {

    // Properties

    private val payNow = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()
    private val success = MutableLiveData<String>()

    // Getters

    fun getPayNow(): LiveData<Boolean> {
        return payNow
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun getSuccess(): LiveData<String> {
        return success
    }

    // Setters

    fun setPayNow(value: Boolean) {
        payNow.value = value
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
            error.value = "Vous devez vous connecter à votre compte avant d'effectuer un achat."
            return
        }
        loading.postValue(true)
        viewModelScope.launch {
            try {
                val response =
                    SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                        .apiService()
                        .createShopItem(token, item.type, item.id)
                loading.value = false
                if (payNow.value != false && response.url != null) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(response.url)
                    )
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(getApplication(), browserIntent, null)
                } else {
                    success.value =
                        "Votre commande a bien été enregistrée, merci de bien vouloir vous présenter à un membre du BDE pour la régler."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                error.value =
                    "Vérifiez que vous êtes bien connecté à internet, que cet élément est encore disponible et que vous ne l'avez pas déjà acheté."
                loading.value = false
            }
        }
    }

    fun dismissError() {
        error.value = null
    }

}