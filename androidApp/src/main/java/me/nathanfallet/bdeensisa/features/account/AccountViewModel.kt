package me.nathanfallet.bdeensisa.features.account

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
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
import me.nathanfallet.bdeensisa.extensions.generateQRCode
import me.nathanfallet.bdeensisa.models.Ticket
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken

class AccountViewModel(
    application: Application,
    code: String?,
    token: String?,
    id: String?,
    val saveToken: (UserToken) -> Unit,
    val showAccount: () -> Unit
) : AndroidViewModel(application) {

    // Properties

    private var qrCode = MutableLiveData<Bitmap>()
    private val tickets = MutableLiveData<List<Ticket>>()

    // Getters

    fun getQrCode(): LiveData<Bitmap> {
        return qrCode
    }

    fun getTickets(): LiveData<List<Ticket>> {
        return tickets
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "account")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "AccountView")
        }

        fetchData(token, id)
        code?.let {
            authenticate(it)
        }
    }

    fun fetchData(token: String?, id: String?) {
        if (token == null || id == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getUserTickets(token, id).let {
                    tickets.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun launchLogin() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().authenticationUrl
            )
        )
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(getApplication(), browserIntent, null)
    }

    fun authenticate(code: String) {
        viewModelScope.launch {
            try {
                val token = SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().authenticate(code)
                saveToken(token)
                showAccount()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun generateQrCode(user: User) {
        viewModelScope.launch {
            val code = "bdeensisa://users/${user.id}".generateQRCode()
            qrCode.value = code
        }
    }

    fun launchPayment(
        token: String?,
        shopItemType: String,
        shopItemId: String,
        itemId: String
    ) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val response = SharedCacheService.getInstance(DatabaseDriverFactory(getApplication()))
                    .apiService().getShopItemPayment(token, shopItemType, shopItemId, itemId)
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(response.url)
                )
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(getApplication(), browserIntent, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}