//
//  NotificationsRepository.swift
//  BDE
//
//  Created by Nathan Fallet on 18/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import FirebaseMessaging
import shared

class NotificationsRepository: INotificationsRepository {
    
    func isTopicSubscribed(topic: String) -> Bool {
        return getSubscription(topic: topic)
    }
    
    func subscribeToTopic(topic: String) {
        setSubscription(topic: topic, subscribed: true)
        Messaging.messaging().subscribe(toTopic: topic)
    }
    
    func unsubscribeFromTopic(topic: String) {
        setSubscription(topic: topic, subscribed: false)
        Messaging.messaging().unsubscribe(fromTopic: topic)
    }
    
    func clearSubscriptions() {
        Messaging.messaging().deleteToken { _ in }
    }
    
    // Internal storage
    
    private let userDefaults = UserDefaults(suiteName: "group.\(Bundle.main.bundleIdentifier ?? "me.nathanfallet.suitebde")")
    
    private func getSubscription(topic: String) -> Bool {
        return userDefaults?.value(forKey: "topic_\(topic)") as? Bool ?? true
    }
    
    private func setSubscription(topic: String, subscribed: Bool) {
        userDefaults?.set(subscribed, forKey: "topic_\(topic)")
        userDefaults?.synchronize()
    }
    
}
