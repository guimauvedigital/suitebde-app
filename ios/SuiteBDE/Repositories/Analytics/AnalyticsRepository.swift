//
//  AnalyticsRepository.swift
//  BDE
//
//  Created by Nathan Fallet on 20/01/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Firebase
import FirebaseAnalytics
import shared

class AnalyticsRepository: IAnalyticsRepository {
    
    func logEvent(name: AnalyticsEventName, params: [AnalyticsEventParameter : String]) {
        Analytics.logEvent(name.value, parameters: Dictionary(uniqueKeysWithValues: params.map { ($0.value, $1) }))
    }
    
    func setUserProperty(name: AnalyticsUserProperty, value: String?) {
        Analytics.setUserProperty(value, forName: name.value)
    }
    
}
