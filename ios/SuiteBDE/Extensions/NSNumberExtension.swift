//
//  NSNumberExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 12/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension NSNumber {
    
    var localizedPrice: String? {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = Locale(identifier: "fr_FR")
        return formatter.string(from: self)
    }
    
}

extension Double {
    
    var localizedPrice: String? {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = Locale(identifier: "fr_FR")
        return formatter.string(from: NSNumber(floatLiteral: self))
    }
    
}
