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

    private let keychain = {
        let dev = Bundle.main.bundleIdentifier?.contains(".dev") == true
        return Keychain(accessGroup: dev ? "group.me.nathanfallet.suitebde.dev" : "group.me.nathanfallet.suitebde")
    }()

    func getToken() -> String? {
        keychain.value(forKey: "token") as? String
    }

    func getRefreshToken() -> String? {
        keychain.value(forKey: "refreshToken") as? String
    }

    func getUserId() -> String? {
        keychain.value(forKey: "userId") as? String
    }

    func getAssociationId() -> String? {
        keychain.value(forKey: "associationId") as? String
    }

    func getFcmToken() -> String? {
        keychain.value(forKey: "fcmToken") as? String
    }

    func setToken(token: String?) {
        let _ = if let token {
            keychain.save(token, forKey: "token")
        } else {
            keychain.remove(forKey: "token")
        }
    }

    func setRefreshToken(token: String?) {
        let _ = if let token {
            keychain.save(token, forKey: "refreshToken")
        } else {
            keychain.remove(forKey: "refreshToken")
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

    func setFcmToken(fcmToken: String?) {
        let _ = if let fcmToken {
            keychain.save(fcmToken, forKey: "fcmToken")
        } else {
            keychain.remove(forKey: "fcmToken")
        }
    }

}
