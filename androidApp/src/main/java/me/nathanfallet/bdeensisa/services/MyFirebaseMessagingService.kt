package me.nathanfallet.bdeensisa.services

import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        StorageService.getInstance(this).sharedPreferences.getString("token", null)?.let {
            CoroutineScope(Job()).launch {
                APIService().sendNotificationToken(it, token)
            }
        }

    }

}