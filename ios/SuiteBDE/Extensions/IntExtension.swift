//
//  IntExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 04/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension Int {
    
    // String for position
    var positionString: String {
        switch self {
        case 1:
            return "\u{1F947}"
        case 2:
            return "\u{1F948}"
        case 3:
            return "\u{1F949}"
        default:
            return "#\(self)"
        }
    }
    
}
