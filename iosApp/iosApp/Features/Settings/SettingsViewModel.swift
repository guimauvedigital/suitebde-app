//
//  SettingsViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 14/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import FirebaseMessaging

class SettingsViewModel: ObservableObject {
    
    @AppStorage("notifications_events", store: StorageService.userDefaults)
    var eventsNotifications = true {
        didSet {
            updateNotifications(enabled: eventsNotifications, topic: "events")
        }
    }
    
    func onAppear() {
        AnalyticsService.shared.log(.screenView(screenName: "settings", screenClass: "SettingsView"))
    }
    
    func updateNotifications(enabled: Bool, topic: String) {
        if enabled {
            Messaging.messaging().subscribe(toTopic: topic)
        } else {
            Messaging.messaging().unsubscribe(fromTopic: topic)
        }
    }
    
}
