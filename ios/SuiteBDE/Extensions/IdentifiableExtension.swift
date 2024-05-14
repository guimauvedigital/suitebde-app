//
//  IdentifiableExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 14/05/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

extension ScannedUser: Identifiable {
    
    public var id: String { "\(associationId)/\(userId)" }
    
}
