//
//  RootViewModel.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class RootViewModel: ObservableObject {
    
    @Published var user: User? {
        didSet {
            // Save user
            if let user {
                StorageService.userDefaults.set(User.companion.toJson(user: user), forKey: "user")
                StorageService.userDefaults.synchronize()
            } else {
                StorageService.userDefaults.removeObject(forKey: "user")
                StorageService.userDefaults.synchronize()
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
    
    func onAppear() {
        // Load user and token, if connected
        if let token = StorageService.keychain.value(forKey: "token") as? String {
            self.token = token
        }
        if let user = StorageService.userDefaults.object(forKey: "user") as? String {
            self.user = User.companion.fromJson(json: user)
        }
        
        // And check token validity
        checkToken()
    }
    
    func checkToken() {
        if let token {
            Task {
                let userToken = try await APIService.shared.checkToken(token: token)
                DispatchQueue.main.async {
                    self.saveToken(userToken: userToken)
                }
            }
        }
    }
    
    func saveToken(userToken: UserToken?) {
        self.token = userToken?.token
        self.user = userToken?.user
    }
    
}
