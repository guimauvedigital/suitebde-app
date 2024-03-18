package me.nathanfallet.suitebde.repositories.notifications

interface INotificationsRepository {

    fun isTopicSubscribed(topic: String): Boolean

    fun subscribeToTopic(topic: String)
    fun unsubscribeFromTopic(topic: String)

}
