//
//  TagExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import CoreNFC

extension NFCISO7816Tag {
    
    var formattedIdentifier: String {
        identifier.map {
            let radix = String($0, radix: 16)
            return String(repeatElement("0", count: 2 - radix.count)) + radix
        }.joined(separator: ":")
    }
    
}
