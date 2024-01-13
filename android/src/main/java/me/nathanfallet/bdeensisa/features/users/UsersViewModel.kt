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
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.models.User

class UsersViewModel(
    application: Application,
    token: String?
) : AndroidViewModel(application) {

    // Properties

    private val search = MutableLiveData<String>()
    private val users = MutableLiveData<List<User>>()
    private val searchUsers = MutableLiveData<List<User>>()
    private val hasMore = MutableLiveData(true)

    // Getters

    fun getSearch(): LiveData<String> {
        return search
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    fun getSearchUsers(): LiveData<List<User>> {
        return searchUsers
    }

    // Setters

    fun setSearch(search: String?) {
        this.search.value = search
    }

    // Methods

    init {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "users")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "UsersView")
        }

        if (search.value?.isNotEmpty() == true) {
            search(token, true)
        } else {
            fetchData(token, true)
        }
    }

    fun fetchData(token: String?, reset: Boolean) {
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getUsers(
                        token,
                        (if (reset) 0 else users.value?.size ?: 0).toLong(),
                        null
                    ).let {
                    if (reset) {
                        users.value = it
                    } else {
                        users.value = (users.value ?: listOf()) + it
                    }
                        hasMore.value = it.isNotEmpty()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun search(token: String?, reset: Boolean) {
        if (search.value?.isNotEmpty() != true) {
            searchUsers.value = null
            return
        }
        if (token == null) {
            return
        }
        viewModelScope.launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(getApplication())).apiService()
                    .getUsers(
                        token,
                        (if (reset) 0 else searchUsers.value?.size ?: 0).toLong(),
                        search.value
                    ).let {
                    if (reset) {
                        searchUsers.value = it
                    } else {
                        searchUsers.value = (searchUsers.value ?: listOf()) + it
                    }
                        hasMore.value = it.isNotEmpty()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMore(token: String?, id: String) {
        if (hasMore.value != true) {
            return
        }
        if (search.value?.isNotEmpty() == true) {
            if (searchUsers.value?.lastOrNull()?.id == id) {
                search(token, false)
            }
        } else {
            if (users.value?.lastOrNull()?.id == id) {
                fetchData(token, false)
            }
        }
    }

}