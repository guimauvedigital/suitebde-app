package me.nathanfallet.bdeensisa.features.users

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.nathanfallet.bdeensisa.models.Ticket
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.services.APIService
import me.nathanfallet.bdeensisa.views.AlertCase

class UserViewModel(
    application: Application,
    token: String?,
    viewedBy: User?,
    user: User,
    val editable: Boolean,
    val isMyAccount: Boolean = false
) : AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData(user)
    private val editing = MutableLiveData(isMyAccount)

    private val hasUnsavedChanges = MutableLiveData(false)
    private val alert = MutableLiveData<AlertCase?>()

    private val image = MutableLiveData<Bitmap>()

    private val firstName = MutableLiveData<String>()
    private val lastName = MutableLiveData<String>()
    private val year = MutableLiveData<String>()
    private val option = MutableLiveData<String>()

    private val expiration = MutableLiveData<LocalDate>()

    private val tickets = MutableLiveData<List<Ticket>>()
    private val paid = MutableLiveData<MutableMap<String, Boolean>>()

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun isEditing(): LiveData<Boolean> {
        return editing
    }

    fun getAlert(): LiveData<AlertCase?> {
        return alert
    }

    fun getImage(): LiveData<Bitmap> {
        return image
    }

    fun getFirstName(): LiveData<String> {
        return firstName
    }

    fun getLastName(): LiveData<String> {
        return lastName
    }

    fun getYear(): LiveData<String> {
        return year
    }

    fun getOption(): LiveData<String> {
        return option
    }

    fun getExpiration(): LiveData<LocalDate> {
        return expiration
    }

    fun getTickets(): LiveData<List<Ticket>> {
        return tickets
    }

    fun getPaid(): LiveData<MutableMap<String, Boolean>> {
        return paid
    }

    // Setters

    fun setAlert(alert: AlertCase?) {
        if (this.alert.value == AlertCase.saved && alert == null) {
            hasUnsavedChanges.value = false
        }
        this.alert.value = alert
    }

    fun setFirstName(firstName: String) {
        this.firstName.value = firstName
        this.hasUnsavedChanges.value = true
    }

    fun setLastName(lastName: String) {
        this.lastName.value = lastName
        this.hasUnsavedChanges.value = true
    }

    fun setYear(year: String) {
        this.year.value = year
        this.hasUnsavedChanges.value = true
    }

    fun setOption(option: String) {
        this.option.value = option
        this.hasUnsavedChanges.value = true
    }

    fun setExpiration(expiration: LocalDate) {
        this.expiration.value = expiration
        this.hasUnsavedChanges.value = true
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "user")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "UserView")
        }

        resetChanges()
        fetchImage(token)
        fetchTickets(token, viewedBy)
    }

    private fun resetChanges() {
        firstName.value = user.value?.firstName
        lastName.value = user.value?.lastName
        year.value = user.value?.year
        option.value = user.value?.option

        expiration.value = user.value?.cotisant?.expiration

        hasUnsavedChanges.value = false
    }

    fun toggleEdit() {
        if (editable) {
            if (editing.value == true && hasUnsavedChanges.value == true) {
                setAlert(AlertCase.cancelling)
                return
            }
            editing.value = !(editing.value ?: false)
            if (editing.value == true) {
                resetChanges()
            }
        }
    }

    fun discardEdit() {
        editing.value = false
    }

    fun fetchImage(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val bytes = APIService().getUserPicture(token, user.value?.id ?: "")
                image.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateImage(token: String?, uri: Uri?, context: Context) {
        if (token == null || uri == null) {
            return
        }
        viewModelScope.launch {
            try {
                val bytes = context.contentResolver.openInputStream(uri)?.use {
                    it.readBytes()
                } ?: ByteArray(0)
                APIService().updateUserPicture(
                    token,
                    user.value?.id ?: "",
                    bytes
                )
                image.value = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                setAlert(AlertCase.saved)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateInfo(token: String?, onUpdate: (User) -> Unit) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val newUser = APIService().updateUser(
                    token,
                    if (isMyAccount) "me" else user.value?.id ?: "",
                    firstName.value ?: "",
                    lastName.value ?: "",
                    year.value ?: "",
                    option.value ?: ""
                )
                user.value = newUser
                setAlert(AlertCase.saved)
                onUpdate(newUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateExpiration(token: String?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                user.value = APIService().updateUser(
                    token,
                    user.value?.id ?: "",
                    expiration.value?.toString() ?: ""
                )
                setAlert(AlertCase.saved)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchTickets(token: String?, viewedBy: User?) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                val tickets = APIService().getUserTickets(token, user.value?.id ?: "")
                this@UserViewModel.tickets.value = tickets
                this@UserViewModel.paid.value = tickets.associate {
                    it.id to (it.paid != null)
                }.toMutableMap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTicket(token: String?, id: String) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                APIService().updateUserTicket(
                    token,
                    user.value?.id ?: "",
                    id,
                    paid.value?.get(id) ?: false
                )
                paid.value = paid.value // Trick to force LiveData to update
                setAlert(AlertCase.saved)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}