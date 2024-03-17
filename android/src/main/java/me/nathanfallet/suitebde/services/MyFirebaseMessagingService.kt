package me.nathanfallet.suitebde.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.usecases.notifications.ISetupNotificationsUseCase
import org.koin.android.ext.android.get
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        get<ITokenRepository>().setFcmToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            get<ISetupNotificationsUseCase>().invoke()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        WebSocketService.getInstance(application).currentConversationId
            ?.takeIf { it == message.data["conversationId"] }
            ?.let { return }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notification = NotificationCompat.Builder(this, "default")
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.logo_transparent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        createNotificationChannel()
        NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("default", "default", importance)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
