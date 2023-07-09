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

class WebSocketService: AbstractWebSocketService {
    
    // Shared instance
    
    static let shared = WebSocketService()
    
    // Properties
    
    private let reachability = try! Reachability()
    
    override var isNetworkAvailable: Bool {
        reachability.connection == .wifi || reachability.connection == .cellular
    }
    
    override var token: String? {
        StorageService.keychain.value(forKey: "token") as? String
    }
    
    override var apiService: APIService {
        CacheService.shared.apiService()
    }
    
    var currentConversationId: String?
    
    // Methods
    
    override init() {
        super.init()
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
    
}
