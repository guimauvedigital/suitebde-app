package com.suitebde.repositories.notifications

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging

class NotificationsRepository(
    context: Context,
) : INotificationsRepository {

    override fun isTopicSubscribed(topic: String): Boolean = getSubscription(topic)

    override fun subscribeToTopic(topic: String) {
        setSubscription(topic, true)
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    override fun unsubscribeFromTopic(topic: String) {
        setSubscription(topic, false)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
    }

    override fun clearSubscriptions() {
        FirebaseMessaging.getInstance().deleteToken()
    }

    // Internal storage

    private val sharedPreferences = context.getSharedPreferences("suitebde", Context.MODE_PRIVATE)

    private fun getSubscription(topic: String) = sharedPreferences.getBoolean("topic_$topic", true)

    private fun setSubscription(topic: String, subscribed: Boolean) = sharedPreferences.edit()
        .putBoolean("topic_$topic", subscribed)
        .apply()

}
