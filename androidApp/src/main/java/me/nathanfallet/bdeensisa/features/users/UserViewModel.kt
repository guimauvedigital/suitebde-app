package me.nathanfallet.bdeensisa.features.users

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
import kotlinx.datetime.LocalDate
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.services.APIService

class UserViewModel(
    application: Application,
    user: User,
    val editable: Boolean
) : AndroidViewModel(application) {

    // Properties

    private val user = MutableLiveData(user)
    private val editing = MutableLiveData(false)

    private val firstName = MutableLiveData<String>(user.firstName)
    private val lastName = MutableLiveData<String>(user.lastName)
    private val year = MutableLiveData<String>(user.year)
    private val option = MutableLiveData<String>(user.option)

    private val expiration = MutableLiveData<LocalDate>(user.cotisant?.expiration)

    // Getters

    fun getUser(): LiveData<User> {
        return user
    }

    fun isEditing(): LiveData<Boolean> {
        return editing
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

    // Setters

    fun setFirstName(firstName: String) {
        this.firstName.value = firstName
    }

    fun setLastName(lastName: String) {
        this.lastName.value = lastName
    }

    fun setYear(year: String) {
        this.year.value = year
    }

    fun setOption(option: String) {
        this.option.value = option
    }

    fun setExpiration(expiration: LocalDate) {
        this.expiration.value = expiration
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "user")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "UserView")
        }
    }

    fun toggleEdit() {
        editing.value = !(editing.value ?: false)
    }

    fun updateInfo(token: String?) {
        token?.let {
            viewModelScope.launch {
                user.value = APIService().updateUser(
                    token,
                    user.value?.id ?: "",
                    firstName.value ?: "",
                    lastName.value ?: "",
                    year.value ?: "",
                    option.value ?: ""
                )
            }
        }
    }

    fun updateExpiration(token: String?) {
        token?.let {
            viewModelScope.launch {
                user.value = APIService().updateUser(
                    token,
                    user.value?.id ?: "",
                    expiration.value?.toString() ?: ""
                )
            }
        }
    }

}