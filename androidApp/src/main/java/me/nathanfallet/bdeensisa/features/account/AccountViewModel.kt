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
import me.nathanfallet.bdeensisa.extensions.generateQRCode
import me.nathanfallet.bdeensisa.models.Ticket
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken
import me.nathanfallet.bdeensisa.services.APIService

class AccountViewModel(
    application: Application,
    code: String?,
    token: String?,
    id: String?,
    val saveToken: (UserToken) -> Unit
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
                APIService().getUserTickets(token, id).let {
                    tickets.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun launchLogin() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(APIService().authenticationUrl)
        )
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(getApplication(), browserIntent, null)
    }

    fun authenticate(code: String) {
        viewModelScope.launch {
            try {
                val token = APIService().authenticate(code)
                saveToken(token)
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

}