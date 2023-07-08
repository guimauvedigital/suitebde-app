//
//  WebSocketService.swift
//  BDE
//
//  Created by Nathan Fallet on 08/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Reachability
import shared

class WebSocketService {
    
    // Shared instance
    
    static let shared = WebSocketService()
    
    // Properties
    
    private let reachability = try! Reachability()
    private var isNetworkAvailable: Bool {
        reachability.connection == .wifi || reachability.connection == .cellular
    }
    
    private var isConnecting = false
    private var session: Ktor_client_coreDefaultClientWebSocketSession?
    
    var onWebSocketMessage: ((Any) -> Void)?
    var onWebSocketMessageConversation: ((Any) -> Void)?
    
    // Methods
    
    init() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(reachabilityChanged(note:)),
            name: .reachabilityChanged,
            object: reachability
        )
        try! reachability.startNotifier()
    }
    
    deinit {
        reachability.stopNotifier()
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func reachabilityChanged(note: Foundation.Notification) {
        // Called when we connect or disconnect from network
        if isNetworkAvailable {
            createWebSocket()
        } else {
            disconnectWebSocket()
        }
    }
    
    func createWebSocket() {
        // Connect if we have a token and socket is not already connected (ie. task running)
        guard let token = StorageService.keychain.value(forKey: "token") as? String,
              self.session == nil,
              !isConnecting
        else { return }
        isConnecting = true
        Task {
            do {
                try await CacheService.shared.apiService().webSocketChat(
                    token: token,
                    onConnected: self.onConnected,
                    onDisconnected: self.onDisconnected,
                    onMessage: self.onMessage
                )
            } catch {
                self.isConnecting = false
            }
        }
    }
    
    func disconnectWebSocket() {
        guard let session else { return }
        Task {
            try await CacheService.shared.apiService().closeWebSocketChat(
                session: session
            )
        }
        self.session = nil
    }
    
    func onConnected(session: Ktor_client_coreDefaultClientWebSocketSession) {
        self.session = session
        self.isConnecting = false
    }
    
    func onDisconnected() {
        self.session = nil
        
        // Try to reconnect if network is available
        if isNetworkAvailable {
            createWebSocket()
        }
    }
    
    func onMessage(message: Any) {
        onWebSocketMessage?(message)
        onWebSocketMessageConversation?(message)
    }
    
}
