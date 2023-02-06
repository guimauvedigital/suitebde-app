//
//  StorageService.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Keychain

struct StorageService {
    
    static let userDefaults = UserDefaults(suiteName: "group.me.nathanfallet.bdeensisa")
    static let keychain = Keychain(accessGroup: "group.me.nathanfallet.bdeensisa")
    
}
