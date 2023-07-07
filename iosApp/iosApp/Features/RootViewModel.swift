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

class RootViewModel: ObservableObject {
    
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
            } else {
                let _ = StorageService.keychain.remove(forKey: "token")
            }
        }
    }
    
    @Published var sheet: RootSheet?
    
    @Published var integrationConfiguration: IntegrationConfiguration?
    
    var socketTimer: Timer?
    var socketTask: Task<(), Error>?
    var onWebSocketMessage: ((Any) -> Void)?
    
    func onAppear() {
        // Load user and token, if connected
        if let token = StorageService.keychain.value(forKey: "token") as? String {
            self.token = token
        }
        if let user = StorageService.userDefaults?.object(forKey: "user") as? String {
            self.user = User.companion.fromJson(json: user)
        }
        
        // And check token validity
        checkToken()
        fetchData()
        createWebSocket()
    }
    
    func fetchData() {
        fetchIntegrationConfiguration()
    }
    
    func checkToken() {
        guard let token else {
            return
        }
        Task {
            let userToken = try await CacheService.shared.apiService().checkToken(token: token)
            DispatchQueue.main.async {
                self.saveToken(userToken: userToken)
            }
        }
    }
    
    func fetchIntegrationConfiguration() {
        guard let token else {
            return
        }
        Task {
            let integrationConfiguration = try await CacheService.shared.apiService().getIntegrationConfiguration(token: token)
            DispatchQueue.main.async {
                self.integrationConfiguration = integrationConfiguration
            }
        }
    }
    
    func deleteAccount() {
        guard let token else {
            return
        }
        Task {
            try await CacheService.shared.apiService().deleteMe(token: token)
            DispatchQueue.main.async {
                self.saveToken(userToken: nil)
            }
        }
    }
    
    func saveToken(userToken: UserToken?) {
        self.token = userToken?.token
        self.user = userToken?.user
    }
    
    func onOpenURL(url: URL) {
        // Scheme
        if url.scheme == "bdeensisa" {
            // Users
            if url.host == "users" {
                downloadUser(id: url.path.trimmingCharacters(
                    in: CharacterSet(arrayLiteral: "/")
                ))
            } else if url.host == "nfc" {
                downloadUserByNFC(id: url.path.trimmingCharacters(
                    in: CharacterSet(arrayLiteral: "/")
                ))
            }
        }
    }
    
    func downloadUser(id: String) {
        guard let token else {
            return
        }
        Task {
            let user = try await CacheService.shared.apiService().getUser(token: token, id: id)
            DispatchQueue.main.async {
                self.sheet = .user(user: user)
            }
        }
    }
    
    func downloadUserByNFC(id: String) {
        guard let token else {
            return
        }
        Task {
            let user = try await CacheService.shared.apiService().getUserByNFC(token: token, id: id)
            DispatchQueue.main.async {
                self.sheet = .user(user: user)
            }
        }
    }
    
    func createWebSocket() {
        guard socketTimer?.isValid != true else {
            return
        }
        socketTimer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { _ in
            // Connect if we have a token and socket is not already connected (ie. task running)
            guard let token = self.token, self.socketTask?.isCancelled != false else {
                return
            }
            self.socketTask = Task {
                try await CacheService.shared.apiService().webSocketChat(
                    token: token,
                    onMessage: self.onMessage
                )
            }
            Task {
                // Allows to detect when task fails (ie. when socket disconnected)
                let _ = await self.socketTask?.result
                self.socketTask = nil
            }
        })
    }
    
    func onMessage(message: Any) {
        onWebSocketMessage?(message)
    }
    
}

enum RootSheet: Identifiable {
    
    case user(user: User)
    
    var id: String {
        switch self {
        case .user(let user):
            return "user:\(user.id)"
        }
    }
    
}
