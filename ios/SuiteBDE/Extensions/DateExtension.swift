//
//  DateExtension.swift
//  BDE
//
//  Created by Nathan Fallet on 03/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension Date {
    
    // Constants
    
    static var tomorrow: Date {
        Calendar.current.nextDate(
            after: Date(),
            matching: DateComponents(hour: 0, minute: 0),
            matchingPolicy: .nextTime
        ) ?? Date()
    }
    
    static var oneYear: Date {
        Calendar.current.nextDate(
            after: Date(),
            matching: DateComponents(month: 7, day: 1),
            matchingPolicy: .nextTime
        ) ?? Date()
    }
    
    static var fiveYears: Date {
        Calendar.current.date(byAdding: .year, value: 4, to: oneYear) ?? Date()
    }
    
    // String for date
    
    var asString: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.string(from: self)
    }
    
    var asStringWithTime: String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(abbreviation: "GMT")
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        return formatter.string(from: self)
    }
    
    var asKotlinxInstant: Kotlinx_datetimeInstant {
        Kotlinx_datetimeInstant.Companion().fromEpochMilliseconds(epochMilliseconds: Int64(timeIntervalSince1970) * 1000)
    }
    
}

extension Kotlinx_datetimeLocalDate {
    
    var asDate: Date {
        Date(timeIntervalSince1970: TimeInterval(toEpochDays() * 24 * 60 * 60))
    }
    
}

extension Kotlinx_datetimeInstant {
    
    var asDate: Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }
    
}
