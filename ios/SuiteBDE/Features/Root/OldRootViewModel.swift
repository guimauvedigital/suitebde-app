//
//  RootViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import FirebaseMessaging

class OldRootViewModel: ObservableObject {
    
    @Published var user: User? {
        didSet {
            // Save user
            if let user {
                StorageService.userDefaults?.set(User.companion.toJson(user: user), forKey: "user")
                StorageService.userDefaults?.synchronize()
                AnalyticsService.shared.updateDimension(.cotisant, value: user.cotisant != nil)
                if user.cotisant != nil {
                    Messaging.messaging().subscribe(toTopic: "cotisants")
                } else {
                    Messaging.messaging().unsubscribe(fromTopic: "cotisants")
                }
            } else {
                StorageService.userDefaults?.removeObject(forKey: "user")
                StorageService.userDefaults?.synchronize()
                Messaging.messaging().unsubscribe(fromTopic: "cotisants")
            }
        }
    }
    @Published var token: String? {
        didSet {
            // Save token
            if let token {
                let _ = StorageService.keychain.save(token, forKey: "token")
                WebSocketService.shared.createWebSocket()
            } else {
                let _ = StorageService.keychain.remove(forKey: "token")
            }
        }
    }
    
}
