//
//  KotlinxDatetimeExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 31/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension Kotlinx_datetimeLocalDate {
    
    var asDate: Date {
        Date(timeIntervalSince1970: TimeInterval(toEpochDays() * 24 * 60 * 60))
    }
    
}

extension Kotlinx_datetimeInstant {
    
    var asDate: Date {
        Date(timeIntervalSince1970: TimeInterval(toEpochMilliseconds() / 1000))
    }
    
}
