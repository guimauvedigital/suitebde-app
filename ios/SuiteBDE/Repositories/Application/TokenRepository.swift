//
//  TokenRepository.swift
//  BDE
//
//  Created by Nathan Fallet on 19/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Keychain
import shared

class TokenRepository: ITokenRepository {
    
    private let keychain = Keychain(accessGroup: "group.\(Bundle.main.bundleIdentifier ?? "me.nathanfallet.suitebde")")
    
    func getToken() -> String? {
        keychain.value(forKey: "token") as? String
    }
    
    func getUserId() -> String? {
        keychain.value(forKey: "userId") as? String
    }
    
    func getAssociationId() -> String? {
        keychain.value(forKey: "associationId") as? String
    }
    
    func setToken(token: String?) {
        let _ = if let token {
            keychain.save(token, forKey: "token")
        } else {
            keychain.remove(forKey: "token")
        }
    }
    
    func setUserId(userId: String?) {
        let _ = if let userId {
            keychain.save(userId, forKey: "userId")
        } else {
            keychain.remove(forKey: "userId")
        }
    }
    
    func setAssociationId(associationId: String?) {
        let _ = if let associationId {
            keychain.save(associationId, forKey: "associationId")
        } else {
            keychain.remove(forKey: "associationId")
        }
    }
    
}
